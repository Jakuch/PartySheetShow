package com.jakuch.PartySheetShow.open5e.spells.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Spell {

    private String name;

    private String level;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("casting_time")
    private String castingTime;

    //TODO add additional spells params that could be needed
}
