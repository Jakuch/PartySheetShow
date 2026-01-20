package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;

public record ChooseOneOf(AbilityName a, AbilityName b, int amount) implements Choice {
    @Override
    public ChoiceType getType() {
        return ChoiceType.ONE_OF;
    }
}

