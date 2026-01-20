package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.model.Open5eData;
import com.jakuch.PartySheetShow.player.character.model.FeatureDataTable;
import com.jakuch.PartySheetShow.player.character.model.FeatureGainedAt;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Open5eFeature extends Open5eData {

    @JsonProperty("feature_type")
    private String type; //TODO for now String, perhaps change it to enum since it could be easier to verify type but thats for later

    @JsonProperty("gained_at")
    private List<FeatureGainedAt> gainedAt;

    @JsonProperty("data_for_class_table")
    private List<FeatureDataTable> data;

    private String classSrdKey;

    public List<Integer> getGainedAtLevels() {
        gainedAt.sort(Comparator.comparingInt(FeatureGainedAt::getLevel));
        return gainedAt.stream().map(FeatureGainedAt::getLevel).toList();
    }

    public int getLowestGainedAtLevel() {
        return getGainedAtLevels().stream().findFirst().orElse(0);
    }

    public String getDisplayGainedAt() {
        var levels = getGainedAtLevels();
        return (levels == null || levels.isEmpty() ? "" : "Gained at levels: " + levels.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
    }
}
