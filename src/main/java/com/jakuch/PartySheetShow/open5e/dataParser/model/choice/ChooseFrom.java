package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;

import java.util.List;

public record ChooseFrom(int count, int amount, List<AbilityName> options) implements Choice {}
