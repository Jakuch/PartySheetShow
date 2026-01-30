package com.jakuch.PartySheetShow.open5e.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Open5eRace extends Open5eData {

    @JsonProperty("is_subspecies")
    private boolean isSubspecies;

    @JsonProperty("traits")
    private List<Open5eRaceTrait> raceTraits;
}
