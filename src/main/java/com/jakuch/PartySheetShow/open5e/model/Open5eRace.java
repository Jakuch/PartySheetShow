package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Open5eRace extends Open5eData {

    @JsonProperty("is_subspecies")
    private boolean isSubspecies;

    @JsonProperty("traits")
    private List<Open5eRaceTrait> raceTraits;
}
