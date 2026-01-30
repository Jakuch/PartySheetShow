package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Open5eFeature extends Open5eData {

    @JsonProperty("feature_type")
    private String type;

    @JsonProperty("gained_at")
    private List<Open5eFeatureGainedAt> gainedAt;

    @JsonProperty("data_for_class_table")
    private List<Open5eFeatureDataTable> data;

    private String classSrdKey;

    public List<Integer> getGainedAtLevels() {
        gainedAt.sort(Comparator.comparingInt(Open5eFeatureGainedAt::getLevel));
        return gainedAt.stream().map(Open5eFeatureGainedAt::getLevel).toList();
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
