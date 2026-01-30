package com.jakuch.PartySheetShow.player.character.form;

import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ProficiencyBonuses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterRaceForm {
    private String key;
    private String name;
    private Map<RaceTraitsParser.RaceTraitsKey, Object> traits;

    public AbilityBonuses getAbilityBonuses() {
        return (AbilityBonuses) traits.get(RaceTraitsParser.RaceTraitsKey.ABILITY_INCREASE);
    }

    public ProficiencyBonuses getProficiencies() {
        return (ProficiencyBonuses) traits.get(RaceTraitsParser.RaceTraitsKey.PROFICIENCY);
    }
}
