package com.jakuch.PartySheetShow.open5e.characterClass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FeatureDataTable {

    private int level;

    @JsonProperty("column_value")
    private String value;
}
