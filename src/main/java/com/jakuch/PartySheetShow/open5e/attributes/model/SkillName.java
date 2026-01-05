package com.jakuch.PartySheetShow.open5e.attributes.model;

import lombok.Getter;

@Getter
public enum SkillName {

    ATHLETICS(AttributeName.STRENGTH, "athletics"),
    ACROBATICS(AttributeName.DEXTERITY, "acrobatics"),
    SLEIGHT_OF_HAND(AttributeName.DEXTERITY, "sleight-of-hand"),
    STEALTH(AttributeName.DEXTERITY, "stealth"),
    ARCANA(AttributeName.INTELLIGENCE, "arcana"),
    HISTORY(AttributeName.INTELLIGENCE, "history"),
    INVESTIGATION(AttributeName.INTELLIGENCE, "investigation"),
    NATURE(AttributeName.INTELLIGENCE, "nature"),
    RELIGION(AttributeName.INTELLIGENCE, "religion"),
    ANIMAL_HANDLING(AttributeName.WISDOM, "animal-handling"),
    INSIGHT(AttributeName.WISDOM, "insight"),
    MEDICINE(AttributeName.WISDOM, "medicine"),
    PERCEPTION(AttributeName.WISDOM, "perception"),
    SURVIVAL(AttributeName.WISDOM, "survival"),
    DECEPTION(AttributeName.CHARISMA, "deception"),
    INTIMIDATION(AttributeName.CHARISMA, "intimidation"),
    PERFORMANCE(AttributeName.CHARISMA, "performance"),
    PERSUASION(AttributeName.CHARISMA, "persuasion");

    private final AttributeName modifier;
    private final String srdKey;

    SkillName(AttributeName modifier, String srdKey) {
        this.modifier = modifier;
        this.srdKey = srdKey;
    }
}
