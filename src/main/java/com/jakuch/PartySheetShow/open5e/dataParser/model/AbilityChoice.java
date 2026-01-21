package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AbilityChoice {
    private AbilityName chosenAbility;
    private int amount;
    private List<AbilityName> options;
}
