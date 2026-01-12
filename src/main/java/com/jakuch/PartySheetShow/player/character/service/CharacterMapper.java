package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.Open5eData;
import com.jakuch.PartySheetShow.open5e.services.AttributeService;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.ClassProficienciesParser;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CharacterMapper {

    private CharacterClassService characterClassService;
    private RaceService raceService;
    private AttributeService attributeService;
    private ClassProficienciesParser classProficienciesParser;

    public CharacterForm toForm(Character character) {
        return CharacterForm.builder()
                .ownerName(character.getAccessRules().owner().getUsername())
                .name(character.getName())
                .maxHealth(character.getHealth().getMax())
                .currentHealth(character.getHealth().getCurrent())
                .level(character.getLevel())
                .armorClass(character.getArmorClass())
                .attributes(character.getAttributes()
                        .stream()
                        .collect(Collectors.toMap(attribute -> AttributeName.findByName(attribute.getName()), Attribute::getValue)))
                .classes(character.getCharacterClasses()
                        .stream()
                        .collect(Collectors.toMap(Open5eData::getSrdKey, characterClass ->  CharacterClassForm.builder()
                                .key(characterClass.getSrdKey())
                                .name(characterClass.getName())
                                .level(characterClass.getLevel().getNumericValue())
                                .build())))
                .build();
    }

    public Character addNew(CharacterForm characterForm) {
        var character = new Character();

        character.setLevel(characterForm.getLevel());
        character.setCurrentExperiencePoints(character.getLevel().getRequiredExperience());

        character.setAttributes(mapAttributes(characterForm));
        character.setInitiativeBonus(InitiativeBonus.builder()
                .value(character.getAttribute(AttributeName.DEXTERITY.getSrdKey()).getBonus())
                .advantage(Advantage.NONE)
                .build());
        character.setName(characterForm.getName());
        character.setArmorClass(characterForm.getArmorClass());

        var formClasses = characterForm.getClasses();
        if(!formClasses.isEmpty()) {
            var classes = formClasses.keySet()
                    .stream()
                    .map(k -> characterClassService.getByKey(k))
                    .flatMap(Optional::stream)
                    .toList();

            classes.forEach(this::mapToClassProficiencies);

            calculateSavingThrows(character, classes.stream().findFirst().orElseThrow(
                    () -> new NoSuchElementException("Missing class")
            ));//for now find first

            character.getCharacterClasses().addAll(classes);
            character.setHealth(setHealthAndCalculateHitDices(characterForm, classes));
        }

        var race = characterForm.getRace();
        if(race != null) {
            raceService.getByKey(race.getKey()).ifPresent(character::setRace);
        }

        character.setWalkingSpeed(characterForm.getWalkingSpeed()); //TODO for now like this it should be taken from race later (its in traits)

        return character;
    }

    public Character update(CharacterForm characterForm, Character character) {
        character.setLevel(characterForm.getLevel());
        updateAttributes(characterForm.getAttributes(), character.getAttributes());
        calculateSkillValues(character.getAttributes(), character.getLevel());
//        character.setCurrentExperiencePoints(characterForm.getCurrentExperiencePoints()); TODO
//        character.setInitiativeBonus(); TODO

//        character.setArmorClass(); TODO - calculate from modifiers + armor

        characterForm.getClasses().forEach((key, classForm) -> {
            var classes = character.getCharacterClasses().stream().peek(characterClass -> {
                if (characterClass.getSrdKey().equalsIgnoreCase(key)) {
                    characterClass.setLevel(Level.findByNumericValue(classForm.getLevel()));
                }
            }).toList();

            calculateSavingThrows(character, classes.stream().findFirst().orElseThrow(
                    () -> new NoSuchElementException("Missing class")
            ));

            updateHealthAndHitDices(character, characterForm, classes);
        });

//        character.setWalkingSpeed(characterForm.getWalkingSpeed()); //TODO that need to be calculated from race/class/features/items...
        return character;
    }

    private void updateAttributes(Map<AttributeName, Integer> attributes, List<Attribute> toUpdate) {
        toUpdate.forEach(attribute -> {
            var value = attributes.get(AttributeName.findByName(attribute.getName()));
            attribute.setValue(value);
        });
    }

    private void updateHealthAndHitDices(Character character, CharacterForm characterForm, List<CharacterClass> classes) {
        var hitDices = calculateHitDices(classes);
        character.getHealth().setCurrent(characterForm.getCurrentHealth());
//        character.getHealth().setMax(); TODO some level up functionality would be needed but thats for later
//        character.getHealth().setTemporary(); TODO
//        character.getHealth().setAvailableHitDices(); TODO add along short/long rest functionality
        character.getHealth().setHitDices(hitDices);
    }

    private void mapToClassProficiencies(CharacterClass characterClass) {
        characterClass.setClassProficiencies(classProficienciesParser.mapToClassProficiencies(characterClass.getClassProficienciesFeature().getDescription()));
    }

    private Health setHealthAndCalculateHitDices(CharacterForm characterForm, List<CharacterClass> classes) {
        var hitDices = calculateHitDices(classes);

        return Health.builder()
                .max(characterForm.getMaxHealth())
                .current(characterForm.getCurrentHealth())
                .availableHitDices(hitDices)
                .hitDices(hitDices)
                .build();
    }

    private Map<Integer, DiceType> calculateHitDices(List<CharacterClass> classes) {
       return classes.stream().collect(Collectors.toMap(characterClass -> characterClass.getLevel().getNumericValue(), CharacterClass::getHitDice));
    }

    private List<Attribute> mapAttributes(CharacterForm characterForm) {
        var attributes = attributeService.getAll();

        characterForm.getAttributes().forEach((attrName, value) -> {
            attributes.forEach(attribute -> {
                if(attribute.getName().equalsIgnoreCase(attrName.getName())) {
                    attribute.setValue(value);
                    attribute.calculateBonusesAndSkills(characterForm.getLevel());
                }
            });
        });

        return attributes;
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
