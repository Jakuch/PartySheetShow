package com.jakuch.PartySheetShow.player.character.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

@Data
public class Skill extends Open5eData {

    private int value;

    @JsonProperty("ability")
    private String modifier;

    private Proficiency proficiency;

    private Advantage advantage = Advantage.NONE;

    private int customBonus; //TODO now does nothing just to not forget about it

    public void setValueWithProficiency(int value, Level level) {
        if(this.proficiency == null) {
            this.proficiency = Proficiency.NONE;
        }

        var proficiencyBonus = Proficiency.calculateProficiencyBonus(this.proficiency, level);
        this.value = value + proficiencyBonus + this.customBonus;
    }
    public void addToValue(int value) {
        this.value += value;
    }
}
