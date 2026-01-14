package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;

public record ChooseOrPair(AbilityName a, AbilityName b, int amount) implements Choice {
}

