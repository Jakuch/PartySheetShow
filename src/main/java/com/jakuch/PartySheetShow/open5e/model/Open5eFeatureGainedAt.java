package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Open5eFeatureGainedAt {

    private int level;

    @JsonProperty("detail")
    private String description;
}
