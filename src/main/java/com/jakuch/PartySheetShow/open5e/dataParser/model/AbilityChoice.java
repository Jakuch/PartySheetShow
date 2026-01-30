package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AbilityChoice implements Choice<AbilityName> {
    private AbilityName chosenAbility;
    private int amount;
    private List<AbilityName> options;

    @Override
    public void setChoice(AbilityName choice) {
        this.chosenAbility = choice;
    }

    @Override
    public AbilityName getChoice() {
        return chosenAbility;
    }

    @Override
    public List<AbilityName>  getOptions() {
        return options;
    }
}
