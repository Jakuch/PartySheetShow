package com.jakuch.PartySheetShow.player.character.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feature {
    private String srdKey;
    private String classSrdKey;
    private FeatureType type;
    private String name;
    private String description;
    private Level gainedAtLevel;

    public String getDisplayGainedAt() {
        return gainedAtLevel != null ? "Gained at level: " + gainedAtLevel.getNumericValue() : "";
    }
}
