package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.open5e.dataParser.model.choice.Choice;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class AbilityBonuses {
    private Map<AbilityName, Integer> fixed;
    private List<Choice> choices;
}
