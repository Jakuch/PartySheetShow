package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

import java.util.List;

@Data
public class Attribute extends Open5eData {

    private int value;

    private int bonus;

    private List<Skill> skills;

    public String getUpperCaseName() {
        return this.name.toUpperCase();
    }
}
