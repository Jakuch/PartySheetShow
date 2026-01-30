package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.model.Open5eData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Open5eSpell extends Open5eData {

    private String level;

    @JsonProperty("casting_time")
    private String castingTime;

    //TODO add additional spells params that could be needed
}
