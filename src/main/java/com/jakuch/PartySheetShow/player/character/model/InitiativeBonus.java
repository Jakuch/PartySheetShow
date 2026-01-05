package com.jakuch.PartySheetShow.player.character.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitiativeBonus {
    private int value;
    private Advantage advantage;

    public InitiativeBonus(int value, Advantage advantage) {
        this.value = value;
        this.advantage = advantage;
    }
}
