package com.jakuch.PartySheetShow.security.model;

import java.util.Arrays;
import java.util.List;

public enum AppUserRole {
    ROLE_ADMIN,
    ROLE_PLAYER,
    ROLE_DM;

    public static List<AppUserRole> getCorrectRoles() {
        return Arrays.asList(ROLE_PLAYER, ROLE_DM);
    }
}
