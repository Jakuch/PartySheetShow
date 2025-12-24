package com.jakuch.partySheetShow.player.character.model;

import com.jakuch.partySheetShow.player.level.model.Level;
import lombok.Getter;

@Getter
public enum Proficiency {
    NONE(""),
    HALF("H"),
    FULL("P"),
    EXPERTISE("E");

    private final String shortName;

    Proficiency(String shortName) {
        this.shortName = shortName;
    }

    public static int calculateProficiencyBonus(Proficiency type, Level level) {
        return switch (type) {
            case NONE -> 0;
            case HALF -> level.getProficiencyBonus() / 2;
            case FULL -> level.getProficiencyBonus();
            case EXPERTISE -> level.getProficiencyBonus() * 2;
        };
    }
}
