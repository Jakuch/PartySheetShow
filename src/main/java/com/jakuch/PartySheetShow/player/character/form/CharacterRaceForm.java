package com.jakuch.PartySheetShow.player.character.form;

import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterRaceForm {
    //form fields
    private String key;
    private String name;
    private List<AbilityName> abilityBonusChoices = new ArrayList<>();

    //sources
    private AbilityBonuses abilityBonuses;
    private List<AbilityName> abilityNamesSelection = new ArrayList<>();
}
