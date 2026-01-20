package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;

public record ChooseAnyExcept(AbilityName excluded, int count, int amount) implements Choice {
    @Override
    public ChoiceType getType() {
        return ChoiceType.ANY_EXCEPT;
    }
}

