package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
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
