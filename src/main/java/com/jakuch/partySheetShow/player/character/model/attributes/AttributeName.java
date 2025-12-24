package com.jakuch.partySheetShow.player.character.model.attributes;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AttributeName {
    STRENGTH("Strength", "STR"),
    DEXTERITY("Dexterity", "DEX"),
    CONSTITUTION("Constitution", "CON"),
    INTELLIGENCE("Intelligence", "INT"),
    WISDOM("Wisdom", "WIS"),
    CHARISMA("Charisma", "CHA"),
    NONE("", "");

    private final String normalName;
    private final String shortName;

    AttributeName(String normalName, String shortName) {
        this.normalName = normalName;
        this.shortName = shortName;
    }

    public static AttributeName findByName(String name) {
        return correctValues().stream().filter(el -> el.normalName.equals(name)).findFirst().orElse(NONE);
    }

    public static AttributeName findByShortName(String shortName) {
        return correctValues().stream().filter(el -> el.shortName.equals(shortName)).findFirst().orElse(NONE);
    }

    public static List<AttributeName> correctValues() {
        return Arrays.asList(STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA);
    }
}