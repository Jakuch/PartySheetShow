package com.jakuch.PartySheetShow.player.character.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SkillName {

    ATHLETICS(AttributeName.STRENGTH, "athletics", "Athletics"),
    ACROBATICS(AttributeName.DEXTERITY, "acrobatics", "Acrobatics"),
    SLEIGHT_OF_HAND(AttributeName.DEXTERITY, "sleight-of-hand", "Sleight of Hand"),
    STEALTH(AttributeName.DEXTERITY, "stealth", "Stealth"),

    ARCANA(AttributeName.INTELLIGENCE, "arcana", "Arcana"),
    HISTORY(AttributeName.INTELLIGENCE, "history", "History"),
    INVESTIGATION(AttributeName.INTELLIGENCE, "investigation", "Investigation"),
    NATURE(AttributeName.INTELLIGENCE, "nature", "Nature"),
    RELIGION(AttributeName.INTELLIGENCE, "religion", "Religion"),

    ANIMAL_HANDLING(AttributeName.WISDOM, "animal-handling", "Animal Handling"),
    INSIGHT(AttributeName.WISDOM, "insight", "Insight"),
    MEDICINE(AttributeName.WISDOM, "medicine", "Medicine"),
    PERCEPTION(AttributeName.WISDOM, "perception", "Perception"),
    SURVIVAL(AttributeName.WISDOM, "survival", "Survival"),

    DECEPTION(AttributeName.CHARISMA, "deception", "Deception"),
    INTIMIDATION(AttributeName.CHARISMA, "intimidation", "Intimidation"),
    PERFORMANCE(AttributeName.CHARISMA, "performance", "Performance"),
    PERSUASION(AttributeName.CHARISMA, "persuasion", "Persuasion");

    private final AttributeName modifier;
    private final String srdKey;
    private final String displayName;

    SkillName(AttributeName modifier, String srdKey, String displayName) {
        this.modifier = modifier;
        this.srdKey = srdKey;
        this.displayName = displayName;
    }


    public static Optional<SkillName> fromName(String name) {
        return Arrays.stream(values())
                .filter(v -> v.displayName.equalsIgnoreCase(name.trim()))
                .findFirst();
    }
    public static SkillName fromStringIgnoreUnknown(String skill) {
        return Arrays.stream(SkillName.values()).filter(s -> skill.equalsIgnoreCase(s.name())).findFirst().orElse(null);
    }
}
