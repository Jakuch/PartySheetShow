package com.jakuch.PartySheetShow.open5e;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Open5eData {

    protected String name;

    @JsonProperty("desc")
    protected String description;

    @JsonProperty("key")
    protected String srdKey;
}
