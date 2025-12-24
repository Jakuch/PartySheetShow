package com.jakuch.partySheetShow.player.character.model;

import lombok.Getter;

@Getter
public enum Advantage {
    ADVANTAGE("A"),
    DISADVANTAGE("D"),
    NONE("");

    private final String shortName;

    Advantage(String shortName) {
        this.shortName = shortName;
    }
}
