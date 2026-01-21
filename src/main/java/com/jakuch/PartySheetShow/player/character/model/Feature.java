package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Feature {
    private String srdKey;
    private String classSrdKey;
    private FeatureType type;
    private String name;
    private String description;
    private Level gainedAtLevel;
    private Map<Level, String> improvedWithLevel;

    public String getDisplayGainedAt() {
        return gainedAtLevel != null ? "Gained at level: " + gainedAtLevel.getNumericValue() : "";
    }

    public String getStringValueByLevel(Level level) {
        return improvedWithLevel.get(level);
    }

    public Integer getIntValueByLevel(Level level) {
        return ParserHelper.safeParseInt(improvedWithLevel.get(level));
    }
}
