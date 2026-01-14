package com.jakuch.PartySheetShow.player.character.service;

import com.jakuch.PartySheetShow.open5e.Open5eData;
import com.jakuch.PartySheetShow.open5e.dataParser.ProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.open5e.services.AbilityService;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CharacterMapper {

    private CharacterClassService characterClassService;
    private RaceService raceService;
    private AbilityService abilityService;
    private ProficienciesParser proficienciesParser;
    private RaceTraitsParser raceTraitsParser;

    public CharacterForm toForm(Character character) {
        return CharacterForm.builder()
                .playerName(character.getPlayerName())
                .name(character.getName())
                .maxHealth(character.getHealth().getMax())
                .currentHealth(character.getHealth().getCurrent())
                .level(character.getLevel())
                .armorClass(character.getArmorClass())
                .abilities(character.getAbilities()
                        .stream()
                        .collect(Collectors.toMap(ability -> AbilityName.findBySrdKey(ability.getSrdKey()), Ability::getValue)))
                .classes(character.getCharacterClasses()
                        .stream()
                        .collect(Collectors.toMap(Open5eData::getSrdKey, characterClass -> CharacterClassForm.builder()
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

        character.setAbilities(mapAbilities(characterForm));
        character.setInitiativeBonus(InitiativeBonus.builder()
                .value(character.getAbility(AbilityName.DEXTERITY.getSrdKey()).getBonus())
                .advantage(Advantage.NONE)
                .build());
        character.setName(characterForm.getName());
        character.setArmorClass(characterForm.getArmorClass());

        var formClasses = characterForm.getClasses();
        if (!formClasses.isEmpty()) {
            var classes = formClasses.keySet()
                    .stream()
                    .map(k -> characterClassService.getByKey(k))
                    .flatMap(Optional::stream)
                    .toList();

            mapToClassProficiencies(classes.getFirst());
            calculateSavingThrows(character, classes.getFirst());

            character.getCharacterClasses().addAll(classes);
            character.setHealth(setHealthAndCalculateHitDices(characterForm, classes));
        }

        var race = characterForm.getRace();
        if (race != null) {
            raceService.getByKey(race.getKey()).ifPresent(r -> {
                character.setRace(r);
                setRaceTraits(character, r);
            });
        }
        character.setWalkingSpeed(character.getWalkingSpeed() + characterForm.getWalkingSpeed()); //TODO for now like this it should be taken from race later (its in traits)

        return character;
    }

    public Character update(CharacterForm characterForm, Character character) {
        character.setLevel(characterForm.getLevel());
        updateAbilities(characterForm.getAbilities(), character.getAbilities());
        calculateSkillValues(character.getAbilities(), character.getLevel());
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

    private void updateAbilities(Map<AbilityName, Integer> abilities, List<Ability> toUpdate) {
        toUpdate.forEach(ability -> {
            var value = abilities.get(AbilityName.findByName(ability.getName()));
            ability.setValue(value);
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
        characterClass.setClassProficiencies(proficienciesParser.mapToClassProficiencies(characterClass.getClassProficienciesFeature().getDescription()));
    }

    private Health setHealthAndCalculateHitDices(CharacterForm characterForm, List<CharacterClass> classes) {
        var hitDices = calculateHitDices(classes);

        return Health.builder()
                .max(characterForm.getMaxHealth())
                .current(characterForm.getMaxHealth())
                .availableHitDices(hitDices)
                .hitDices(hitDices)
                .build();
    }

    private Map<DiceType, Integer> calculateHitDices(List<CharacterClass> classes) {
        return classes.stream().collect(Collectors.toMap(CharacterClass::getHitDice, characterClass -> characterClass.getLevel().getNumericValue()));
    }

    private List<Ability> mapAbilities(CharacterForm characterForm) {
        var abilities = abilityService.getAll();

        characterForm.getAbilities().forEach((attrName, value) -> {
            abilities.forEach(ability -> {
                if (ability.getName().equalsIgnoreCase(attrName.getName())) {
                    ability.setValue(value);
                    ability.calculateBonusesAndSkills(characterForm.getLevel());
                }
            });
        });

        return abilities;
    }

    private void calculateSkillValues(List<Ability> abilities, Level level) {
        abilities.forEach(ability ->
                ability.getSkills().forEach(skill -> skill.setValueWithProficiency(ability.getBonus(), level))
        );
    }

    private void calculateSavingThrows(Character character, CharacterClass characterClass) {
        var savingThrows = AbilityName.correctValues()
                .stream()
                .map(abilityName -> {
                    var ability = character.getAbility(abilityName.getSrdKey());
                    var savingThrow = new SavingThrow(ability.getName() + " saving throw");
                    characterClass.getClassProficiencies().savingThrows().forEach(st -> {
                        if (st.getSrdKey().equalsIgnoreCase(ability.getSrdKey())) {
                            savingThrow.setProficiency(Proficiency.FULL);
                            savingThrow.setValueWithProficiency(ability.getBonus(), character.getLevel());
                        }
                    });

                    return savingThrow;
                })
                .toList();

        character.setSavingThrows(savingThrows);
    }

    private void setRaceTraits(Character character, Race race) {
        var raceTraitsKeyObjectMap = raceTraitsParser.parseRaceTraits(race);
        //TODO
    }
}
