package com.jakuch.partySheetShow.open5e.characterClass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FeatureGainedAt {

    private int level;

    @JsonProperty("detail")
    private String description;
}
