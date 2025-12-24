package com.jakuch.partySheetShow.open5e.spells.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.partySheetShow.open5e.Open5eData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Spell extends Open5eData {

    private String level;

    @JsonProperty("casting_time")
    private String castingTime;

    //TODO add additional spells params that could be needed
}
