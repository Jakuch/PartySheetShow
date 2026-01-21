package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Open5eFeatureDataTable {

    private int level;

    @JsonProperty("column_value")
    private String value;
}
