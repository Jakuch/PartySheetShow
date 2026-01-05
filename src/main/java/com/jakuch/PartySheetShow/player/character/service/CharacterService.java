package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.attributes.model.AttributeName;
import com.jakuch.PartySheetShow.open5e.attributes.service.AttributeService;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.characterClass.service.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.races.service.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.character.model.savingThrows.SavingThrow;
import com.jakuch.PartySheetShow.player.character.repository.CharacterRepository;
import com.jakuch.PartySheetShow.player.dice.model.DiceType;
import com.jakuch.PartySheetShow.player.level.model.Level;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterService {

    private CharacterRepository characterRepository;
    private CharacterClassService characterClassService;
    private RaceService raceService;
    private AttributeService attributeService;

    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    public Optional<Character> findById(String id) {
        return characterRepository.findById(id);
    }

    public void saveCharacter(CharacterForm characterForm) {
        characterRepository.save(toCharacter(characterForm));
    }

    public void deleteCharacter(String id) {
        characterRepository.deleteById(id);
    }

    private Character toCharacter(CharacterForm characterForm) {
        var character = new Character();

        character.setLevel(characterForm.getLevel());
        character.setAttributes(attributeService.getAll());

        setAttributeValues(character.getAttributes(), characterForm);
        calculateSkillValues(character.getAttributes(), character.getLevel());
        calculateSavingThrows(character);
        character.setCurrentExperiencePoints(character.getLevel().getRequiredExperience());

        character.setInitiativeBonus(InitiativeBonus.builder()
                .value(character.getAttribute(AttributeName.DEXTERITY.getSrdKey()).getBonus())
                .advantage(Advantage.NONE)
                .build());
        character.setName(characterForm.getName());
        character.setArmorClass(characterForm.getArmorClass());

        var classes = characterForm.getCharacterClasses()
                .stream()
                .map(CharacterClassForm::getKey)
                .map(el -> characterClassService.getByKey(el))
                .flatMap(Optional::stream)
                .toList();

        character.getCharacterClasses().addAll(classes);
        character.setHealth(setHealthAndCalculateHitDices(characterForm, classes));

        raceService.getByKey(characterForm.getRace().getKey()).ifPresent(character::setRace);

        character.setWalkingSpeed(characterForm.getWalkingSpeed()); //TODO for now like this it should be taken from race later (its in traits)

        return character;
    }

    private Health setHealthAndCalculateHitDices(CharacterForm characterForm, List<CharacterClass> classes) {
        var hitDices = new HashMap<Integer, DiceType>();
        classes.forEach(characterClass -> hitDices.put(characterClass.getLevel().getNumericValue(), characterClass.getHitPoints().getDiceType()));

        return Health.builder()
                .max(characterForm.getMaxHealth())
                .current(characterForm.getMaxHealth())
                .hitDices(hitDices)
                .build();
    }

    private void setAttributeValues(List<com.jakuch.PartySheetShow.open5e.attributes.model.Attribute> attributes, CharacterForm characterForm) {
        attributes.forEach(attribute -> {
            switch (attribute.getName()) {
                case "Strength" -> attribute.setValue(characterForm.getStrength());
                case "Dexterity" -> attribute.setValue(characterForm.getDexterity());
                case "Constitution" -> attribute.setValue(characterForm.getConstitution());
                case "Intelligence" -> attribute.setValue(characterForm.getIntelligence());
                case "Wisdom" -> attribute.setValue(characterForm.getWisdom());
                case "Charisma" -> attribute.setValue(characterForm.getCharisma());
            }

            calculateAttributeBonus(attribute);
        });
    }

    private void calculateAttributeBonus(com.jakuch.PartySheetShow.open5e.attributes.model.Attribute attribute) {
        attribute.setBonus((attribute.getValue() - 10) / 2);
    }

    private void calculateSkillValues(List<com.jakuch.PartySheetShow.open5e.attributes.model.Attribute> attributes, Level level) {
        attributes.forEach(attribute ->
                attribute.getSkills().forEach(skill -> {
                    int baseValue = attribute.getBonus();
                    int proficiencyBonus = Proficiency.calculateProficiencyBonus(skill.getProficiency(), level);
                    skill.setValue(baseValue + proficiencyBonus);
                })
        );
    }

    private void calculateSavingThrows(Character character) {
        var savingThrows = AttributeName.correctValues()
                .stream()
                .map(attributeName -> {
                    var attribute = character.getAttribute(attributeName.getSrdKey());
                    return new SavingThrow(attribute.getName() + " saving throw", attribute.getBonus());
                })
                .toList();

        character.setSavingThrows(savingThrows);
    }
}
