package com.jakuch.PartySheetShow.player.character.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AbilityName {
    STRENGTH("Strength", "STR"),
    DEXTERITY("Dexterity", "DEX"),
    CONSTITUTION("Constitution", "CON"),
    INTELLIGENCE("Intelligence", "INT"),
    WISDOM("Wisdom", "WIS"),
    CHARISMA("Charisma", "CHA"),
    NONE("", "");

    private final String name;
    private final String srdKey;

    AbilityName(String name, String srdKey) {
        this.name = name;
        this.srdKey = srdKey;
    }

    public static AbilityName findByName(String name) {
        return correctValues().stream()
                .filter(el -> el.name.equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(NONE);
    }

    public static AbilityName findBySrdKey(String srdKey) {
        return correctValues().stream().filter(el -> el.srdKey.equalsIgnoreCase(srdKey)).findFirst().orElse(NONE);
    }

    public static List<AbilityName> correctValues() {
        return Arrays.asList(STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA);
    }
}