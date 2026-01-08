package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

import java.util.List;

@Data
public class Race extends Open5eData {

    @JsonProperty("is_subspecies")
    private boolean isSubspecies;

    @JsonProperty("traits")
    private List<RaceTrait> raceTraits;
}
