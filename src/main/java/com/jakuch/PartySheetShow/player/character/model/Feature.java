package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
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
    private Map<Level, List<String>> improvedWithLevel;

    public String getDisplayGainedAt() {
        return gainedAtLevel != null ? "Gained at level: " + gainedAtLevel.getNumericValue() : "";
    }

    public List<String> getStringValueListByLevel(Level level) {
        return improvedWithLevel.get(level);
    }

    public List<Integer> getIntValueListByLevel(Level level) {
        return improvedWithLevel.get(level).stream().map(ParserHelper::safeParseInt).toList();
    }

    public Integer getLowestValue(Level level) {
        return getIntValueListByLevel(level).stream().min(Comparator.comparingInt(Integer::intValue)).orElse(0);
    }
}
