package com.jakuch.PartySheetShow.open5e.characterClass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jakuch.PartySheetShow.dice.model.DiceType;
import com.jakuch.PartySheetShow.level.model.Level;
import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacterClass extends Open5eData {

    private Level level = Level.FIRST; //TODO default for now to make it work

    @JsonProperty("subclass_of")
    private Subclass subclass;

    @JsonProperty("hit_dice")
    private DiceType hitDice;

}
