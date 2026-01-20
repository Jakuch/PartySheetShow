package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

public sealed interface Choice permits ChooseAny, ChooseAnyExcept, ChooseFrom, ChooseOneOf {

    ChoiceType getType();
}
