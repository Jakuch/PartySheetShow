package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.services.AttributeService;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.ClassProficienciesParser;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CharacterMapper {

    private CharacterClassService characterClassService;
    private RaceService raceService;
    private AttributeService attributeService;
    private ClassProficienciesParser classProficienciesParser;

    public Character fromForm(CharacterForm characterForm) {
        var character = new Character();

        character.setLevel(characterForm.getLevel());
        character.setAttributes(attributeService.getAll());

        setAttributeValues(character.getAttributes(), characterForm);
        calculateSkillValues(character.getAttributes(), character.getLevel());
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

        classes.forEach(this::mapToClassProficiencies);
        calculateSavingThrows(character, classes.stream().findFirst().orElseThrow(
                () -> new NoSuchElementException("Missing class")
        ));//for now find first

        character.getCharacterClasses().addAll(classes);
        character.setHealth(setHealthAndCalculateHitDices(characterForm, classes));

        raceService.getByKey(characterForm.getRace().getKey()).ifPresent(character::setRace);

        character.setWalkingSpeed(characterForm.getWalkingSpeed()); //TODO for now like this it should be taken from race later (its in traits)

        return character;
    }

    public CharacterForm toForm(Character character) {
        return CharacterForm.builder()
                .build();
    }

    private void mapToClassProficiencies(CharacterClass characterClass) {
        characterClass.setClassProficiencies(classProficienciesParser.mapToClassProficiencies(characterClass.getClassProficienciesFeature().getDescription()));
    }

    private Health setHealthAndCalculateHitDices(CharacterForm characterForm, List<CharacterClass> classes) {
        var hitDices = new HashMap<Integer, DiceType>();
        classes.forEach(characterClass -> hitDices.put(characterClass.getLevel().getNumericValue(), characterClass.getHitDice()));

        return Health.builder()
                .max(characterForm.getMaxHealth())
                .current(characterForm.getMaxHealth())
                .hitDices(hitDices)
                .build();
    }

    private void setAttributeValues(List<Attribute> attributes, CharacterForm characterForm) {
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

    private void calculateAttributeBonus(Attribute attribute) {
        attribute.setBonus((attribute.getValue() - 10) / 2);
    }

    private void calculateSkillValues(List<Attribute> attributes, Level level) {
        attributes.forEach(attribute ->
                attribute.getSkills().forEach(skill -> skill.setValueWithProficiency(attribute.getBonus(), level))
        );
    }

    private void calculateSavingThrows(Character character, CharacterClass characterClass) {
        var savingThrows = AttributeName.correctValues()
                .stream()
                .map(attributeName -> {
                    var attribute = character.getAttribute(attributeName.getSrdKey());
                    var savingThrow = new SavingThrow(attribute.getName() + " saving throw");
                    characterClass.getClassProficiencies().savingThrows().forEach(st -> {
                        if(st.getSrdKey().equalsIgnoreCase(attribute.getSrdKey())) {
                            savingThrow.setProficiency(Proficiency.FULL);
                            savingThrow.setValueWithProficiency(attribute.getBonus(), character.getLevel());
                        }
                    });

                    return savingThrow;
                })
                .toList();

        character.setSavingThrows(savingThrows);
    }
}
