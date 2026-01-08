package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

import java.util.List;

@Data
public class Feature extends Open5eData {

    @JsonProperty("feature_type")
    private String type; //TODO for now String, perhaps change it to enum since it could be easier to verify type but thats for later

    @JsonProperty("gained_at")
    private List<FeatureGainedAt> gainedAt;

    @JsonProperty("data_for_class_table")
    private List<FeatureDataTable> data;
}
