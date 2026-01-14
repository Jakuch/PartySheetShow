package com.jakuch.PartySheetShow.player.character.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SkillName {

    ATHLETICS(AbilityName.STRENGTH, "athletics", "Athletics"),
    ACROBATICS(AbilityName.DEXTERITY, "acrobatics", "Acrobatics"),
    SLEIGHT_OF_HAND(AbilityName.DEXTERITY, "sleight-of-hand", "Sleight of Hand"),
    STEALTH(AbilityName.DEXTERITY, "stealth", "Stealth"),

    ARCANA(AbilityName.INTELLIGENCE, "arcana", "Arcana"),
    HISTORY(AbilityName.INTELLIGENCE, "history", "History"),
    INVESTIGATION(AbilityName.INTELLIGENCE, "investigation", "Investigation"),
    NATURE(AbilityName.INTELLIGENCE, "nature", "Nature"),
    RELIGION(AbilityName.INTELLIGENCE, "religion", "Religion"),

    ANIMAL_HANDLING(AbilityName.WISDOM, "animal-handling", "Animal Handling"),
    INSIGHT(AbilityName.WISDOM, "insight", "Insight"),
    MEDICINE(AbilityName.WISDOM, "medicine", "Medicine"),
    PERCEPTION(AbilityName.WISDOM, "perception", "Perception"),
    SURVIVAL(AbilityName.WISDOM, "survival", "Survival"),

    DECEPTION(AbilityName.CHARISMA, "deception", "Deception"),
    INTIMIDATION(AbilityName.CHARISMA, "intimidation", "Intimidation"),
    PERFORMANCE(AbilityName.CHARISMA, "performance", "Performance"),
    PERSUASION(AbilityName.CHARISMA, "persuasion", "Persuasion");

    private final AbilityName modifier;
    private final String srdKey;
    private final String displayName;

    SkillName(AbilityName modifier, String srdKey, String displayName) {
        this.modifier = modifier;
        this.srdKey = srdKey;
        this.displayName = displayName;
    }


    public static Optional<SkillName> fromName(String name) {
        if(name.equalsIgnoreCase("Animal")) {
            return Optional.of(SkillName.ANIMAL_HANDLING);
        }
        if(name.equalsIgnoreCase("Sleight")) {
            return Optional.of(SkillName.SLEIGHT_OF_HAND);
        }

        return Arrays.stream(values())
                .filter(v -> v.displayName.equalsIgnoreCase(name.trim()))
                .findFirst();
    }
}
