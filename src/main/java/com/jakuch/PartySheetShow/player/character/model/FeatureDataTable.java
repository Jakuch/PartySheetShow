package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FeatureDataTable {

    private int level;

    @JsonProperty("column_value")
    private String value;
}
