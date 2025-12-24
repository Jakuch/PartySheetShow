package com.jakuch.partySheetShow.open5e.characterClass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.partySheetShow.player.dice.model.DiceType;
import lombok.Data;

@Data
public class HitPoints {

    @JsonProperty("hit_dice")
    private DiceType diceType;

    @JsonProperty("hit_points_at_1st_level")
    private String firstLevelDesc;

    @JsonProperty("hit_points_at_higher_levels")
    private String higherLevelDesc;

}
