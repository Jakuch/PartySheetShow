package com.jakuch.PartySheetShow.open5e.attributes.model;

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

    private final String name;
    private final String srdKey;

    AttributeName(String name, String srdKey) {
        this.name = name;
        this.srdKey = srdKey;
    }

    public static AttributeName findByName(String name) {
        return correctValues().stream().filter(el -> el.name.equals(name)).findFirst().orElse(NONE);
    }

    public static AttributeName findBySrdKey(String srdKey) {
        return correctValues().stream().filter(el -> el.srdKey.equalsIgnoreCase(srdKey)).findFirst().orElse(NONE);
    }

    public static List<AttributeName> correctValues() {
        return Arrays.asList(STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA);
    }
}