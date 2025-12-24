package com.jakuch.PartySheetShow.player.level.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Level {
    FIRST(1, 0, 300, 0, 2),
    SECOND(2, 300, 900, 300, 2),
    THIRD(3, 900, 2700, 600, 2),
    FOURTH(4, 2700, 6500, 1800, 2),
    FIFTH(5, 6500, 14000, 3800, 3),
    SIXTH(6, 14000, 23000, 5100, 3),
    SEVENTH(7, 23000, 34000, 9000, 3),
    EIGHTH(8, 34000, 48000, 11000, 3),
    NINTH(9, 48000, 64000, 14000, 4),
    TENTH(10, 64000, 85000, 16000, 4),
    ELEVENTH(11, 85000, 100000, 21000, 4),
    TWELFTH(12, 100000, 120000, 15000, 4),
    THIRTEENTH(13, 120000, 140000, 20000, 5),
    FOURTEENTH(14, 140000, 165000, 20000, 5),
    FIFTEENTH(15, 165000, 195000, 25000, 5),
    SIXTEENTH(16, 195000, 225000, 30000, 5),
    SEVENTEENTH(17, 225000, 265000, 30000, 6),
    EIGHTEENTH(18, 265000, 305000, 40000, 6),
    NINETEENTH(19, 305000, 355000, 40000, 6),
    TWENTIETH(20, 355000, 0, 50000, 6);

    private final int numericValue;
    private final int requiredExperience;
    private final int nextLevelExperience;
    private final int experienceDifference;
    private final int proficiencyBonus;

    Level(int numericValue, int requiredExperience, int nextLevelExperience, int experienceDifference, int proficiencyBonus) {
        this.numericValue = numericValue;
        this.requiredExperience = requiredExperience;
        this.nextLevelExperience = nextLevelExperience;
        this.experienceDifference = experienceDifference;
        this.proficiencyBonus = proficiencyBonus;
    }

    public static Level findByNumericValue(int value) {
        return Arrays.stream(values()).filter(it -> it.numericValue == value).findFirst().orElse(FIRST);
    }

    public static Level getByExperiencePoints(int experiencePoints) {
        if (experiencePoints < 0) {
            return FIRST;
        }
        return Arrays.stream(values()).filter(it -> experiencePoints >= it.requiredExperience &&
                experiencePoints < it.nextLevelExperience).findFirst().orElse(FIRST);
    }
}
