package com.jakuch.PartySheetShow.player.character.model;

import java.util.Arrays;
import java.util.List;

public enum Size {
    TINY,
    SMALL,
    MEDIUM,
    LARGE,
    HUGE,
    GARGANTUAN;

    public static List<String> asStringList() {
        return Arrays.stream(values()).map(Enum::name).toList();
    }
}
