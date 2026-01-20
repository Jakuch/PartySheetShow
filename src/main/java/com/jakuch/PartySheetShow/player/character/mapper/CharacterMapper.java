package com.jakuch.PartySheetShow.player.character.mapper;

import com.jakuch.PartySheetShow.open5e.dataParser.ProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.open5e.model.Open5eData;
import com.jakuch.PartySheetShow.open5e.model.Open5eSkill;
import com.jakuch.PartySheetShow.open5e.services.AbilityService;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.character.model.skill.*;
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
    private AbilityService abilityService;
    private ProficienciesParser proficienciesParser;

    private static final int PASSIVE_SENSES_BASE_VALUE = 10;

    public CharacterForm toForm(Character character) {
        return CharacterForm.builder()
                .playerName(character.getPlayerName())
                .name(character.getName())
                .maxHealth(character.getHealth().getMax())
                .currentHealth(character.getHealth().getCurrent())
                .temporaryHealth(character.getHealth().getTemporary())
                .level(character.getLevel())
                .armorClass(character.getArmorClass())
                .abilities(character.getAbilities()
                        .stream()
                        .collect(Collectors.toMap(Ability::getName, Ability::getValue)))
                .classes(character.getCharacterClasses()
                        .stream()
                        .collect(Collectors.toMap(Open5eData::getSrdKey, characterClass -> CharacterClassForm.builder()
                                .key(characterClass.getSrdKey())
                                .name(characterClass.getName())
                                .level(characterClass.getLevel())
                                .build())))
                .build();
    }

    public Character addNew(CharacterForm characterForm) {
        var character = new Character();

        character.setLevel(characterForm.getLevel());
        character.setCurrentExperiencePoints(character.getLevel().getRequiredExperience());

        character.setAbilities(mapAbilities(characterForm));
        character.setPassiveSenses(mapPassiveSkills(character));
        character.setInitiative(mapInitiative(character.getAbility(AbilityName.DEXTERITY.getSrdKey())));
        character.setName(characterForm.getName());
        character.setArmorClass(characterForm.getArmorClass());

        var formClasses = characterForm.getClasses();
        if (!formClasses.isEmpty()) {
            var classes = formClasses.keySet()
                    .stream()
                    .map(k -> characterClassService.getByKey(k))
                    .flatMap(Optional::stream)
                    .toList();

            var firstClass = classes.getFirst();
            firstClass.setClassProficiencies(mapToClassProficiencies(firstClass));;

            character.setSavingThrows(mapSavingThrows(character.getAbilities(), firstClass));
            character.getCharacterClasses().addAll(classes);
            character.setHealth(mapHealthAndCalculateHitDices(characterForm, classes));
        }

        var race = characterForm.getRace();
        if (race != null) {
            raceService.getMappedRaceByKey(race.getKey()).ifPresent(r -> {
                character.setRace(r);
                character.getAbilities().forEach(ability -> {
                    var fixedBonus = race.getAbilityBonuses().getFixed().get(ability.getName());
                    ability.addValue(fixedBonus);
                    var choiceBonus = race.getAbilityBonusChoices().stream().filter(abilityName -> abilityName.equals(ability.getName())).count();
                    ability.addValue((int) choiceBonus);
                });
            });
        }
        character.setWalkingSpeed((Integer) character.getRace().traits().get(RaceTraitsParser.RaceTraitsKey.SPEED) + characterForm.getWalkingSpeed()); //TODO for now like this it should be taken from race later (its in traits)

        return character;
    }

    public Character update(CharacterForm characterForm, Character character) {
        character.setLevel(characterForm.getLevel());
        updateAbilities(characterForm.getAbilities(), character.getAbilities());
        calculateSkillValues(character.getAbilities());
//        character.setCurrentExperiencePoints(characterForm.getCurrentExperiencePoints()); TODO
//        character.setInitiativeBonus(); TODO

//        character.setArmorClass(); TODO - calculate from modifiers + armor

        characterForm.getClasses().forEach((key, classForm) -> {
            var classes = character.getCharacterClasses().stream().peek(characterClass -> {
                if (characterClass.getSrdKey().equalsIgnoreCase(key)) {
                    characterClass.setLevel(Level.findByNumericValue(classForm.getLevel().getNumericValue()));
                }
            }).toList();

            character.setSavingThrows(mapSavingThrows(character.getAbilities(), classes.getFirst()));
            character.setHealth(updateHealthAndHitDices(character.getHealth(), characterForm, classes));
        });

//        character.setWalkingSpeed(characterForm.getWalkingSpeed()); //TODO that need to be calculated from race/class/features/items...
        return character;
    }

    private List<Ability> mapAbilities(CharacterForm characterForm) {
        var open5eAbilities = abilityService.getAll();
        var abilities = new ArrayList<Ability>();

        characterForm.getAbilities().forEach((abilityName, value) ->
                open5eAbilities.forEach(open5eAbility -> {
                    if (open5eAbility.getName().equalsIgnoreCase(abilityName.getName())) {
                        var ability = Ability.builder()
                                .name(abilityName)
                                .value(value)
                                .skills(mapSkills(open5eAbility.getSkills()))
                                .setSkillValues()
                                .build();

                        abilities.add(ability);
                    }
                }));

        return abilities;
    }

    private List<Skill> mapSkills(List<Open5eSkill> skills) {
        return skills.stream().map(open5eSkill -> {
            var skill = new Skill();
            skill.setSkillName(SkillName.fromNameOrSrdKey(open5eSkill.getSrdKey()).orElse(null));

            return skill;
        }).toList();
    }

    private List<PassiveSkill> mapPassiveSkills(Character character) {
        return List.of(
                mapToPassiveSkill(character.getSkill(SkillName.PERCEPTION.getSrdKey())),
                mapToPassiveSkill(character.getSkill(SkillName.INVESTIGATION.getSrdKey())),
                mapToPassiveSkill(character.getSkill(SkillName.INSIGHT.getSrdKey()))
        );
    }

    private PassiveSkill mapToPassiveSkill(Skill skill) {
        var passiveSkill = new PassiveSkill();
        passiveSkill.setSkillName(skill.getSkillName());
        passiveSkill.setValue(PASSIVE_SENSES_BASE_VALUE + skill.getValue());
        passiveSkill.setProficiency(skill.getProficiency());
        return passiveSkill;
    }

    private Initiative mapInitiative(Ability ability) {
        var initiative = new Initiative();
        initiative.setValue(ability.getBonus());
        return initiative;
    }

    private ClassProficiencies mapToClassProficiencies(Open5eClass open5eClass) {
        return proficienciesParser.mapToClassProficiencies(open5eClass.getClassProficienciesFeature().getDescription());
    }

    private Health mapHealthAndCalculateHitDices(CharacterForm characterForm, List<Open5eClass> classes) {
        var hitDices = calculateHitDices(classes);

        return Health.builder()
                .max(characterForm.getMaxHealth())
                .current(characterForm.getMaxHealth())
                .availableHitDices(hitDices)
                .hitDices(hitDices)
                .build();
    }

    private List<SavingThrow> mapSavingThrows(List<Ability> abilities, Open5eClass open5eClass) {
        return abilities.stream().map(ability -> {
            var savingThrow = new SavingThrow();
            savingThrow.setAbilityName(ability.getName());
            savingThrow.setValue(ability.getBonus());

            open5eClass.getClassProficiencies().savingThrows().forEach(st -> {
                if (st.getSrdKey().equalsIgnoreCase(ability.getName().getSrdKey())) {
                    savingThrow.setProficiency(Proficiency.FULL);
                }
            });

            return savingThrow;
        }).toList();
    }

    private void updateAbilities(Map<AbilityName, Integer> abilities, List<Ability> toUpdate) {
        toUpdate.forEach(ability -> {
            var value = abilities.get(AbilityName.findByNameOrSrdKey(ability.getName().getSrdKey()));
            ability.setValue(value);
        });
    }

    private Health updateHealthAndHitDices(Health health, CharacterForm characterForm, List<Open5eClass> classes) {
        health.setMax(characterForm.getMaxHealth());
        health.setTemporary(characterForm.getTemporaryHealth());
//        health.setMax(health.getMax()); TODO some level up functionality would be needed but thats for later
//        health.setAvailableHitDices(health.getAvailableHitDices()); TODO add along short/long rest functionality
        health.setHitDices(calculateHitDices(classes));

        return health;
    }

    private Map<DiceType, Integer> calculateHitDices(List<Open5eClass> classes) {
        return classes.stream().collect(Collectors.toMap(Open5eClass::getHitDice, characterClass -> characterClass.getLevel().getNumericValue()));
    }


    private void calculateSkillValues(List<Ability> abilities) {
        abilities.forEach(ability ->
                ability.getSkills().forEach(skill -> skill.setValue(ability.getBonus()))
        );
    }
}
