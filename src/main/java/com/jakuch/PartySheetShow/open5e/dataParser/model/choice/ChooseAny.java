package com.jakuch.PartySheetShow.open5e.dataParser.model.choice;

public record ChooseAny(int count, int amount, boolean any) implements Choice {
    @Override
    public ChoiceType getType() {
        return ChoiceType.ANY;
    }
}
