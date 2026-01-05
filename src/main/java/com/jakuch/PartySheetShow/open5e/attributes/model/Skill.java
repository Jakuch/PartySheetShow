package com.jakuch.PartySheetShow.open5e.attributes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import com.jakuch.PartySheetShow.player.character.model.Advantage;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import lombok.Data;

@Data
public class Skill extends Open5eData {

    private int value;

    @JsonProperty("ability")
    private String modifier;

    private Proficiency proficiency = Proficiency.NONE;

    private Advantage advantage = Advantage.NONE;

    private int customBonus; //TODO now does nothing just to not forget about it

}
