package com.jakuch.partySheetShow.security.model;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN,
    PLAYER,
    DM;

    public static List<UserRole> getCorrectRoles() {
        return Arrays.asList(PLAYER, DM);
    }
}
