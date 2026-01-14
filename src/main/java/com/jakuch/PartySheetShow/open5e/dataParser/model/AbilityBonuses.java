package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.Choice;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class AbilityBonuses {
    private String rawText;
    private HashMap<AbilityName, Integer> fixed;
    private List<Choice> choices;
}
