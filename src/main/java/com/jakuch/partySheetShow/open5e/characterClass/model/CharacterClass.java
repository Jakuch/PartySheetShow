package com.jakuch.partySheetShow.open5e.characterClass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.partySheetShow.player.level.model.Level;
import com.jakuch.partySheetShow.open5e.Open5eData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CharacterClass extends Open5eData {

    private Level level = Level.FIRST; //TODO default for now to make it work

    @JsonProperty("subclass_of")
    private Subclass subclass;

    @JsonProperty("hit_points")
    private HitPoints hitPoints;

    private List<Feature> features;

}
