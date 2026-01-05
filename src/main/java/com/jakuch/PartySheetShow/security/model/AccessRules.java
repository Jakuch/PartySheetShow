package com.jakuch.PartySheetShow.security.model;

import java.util.List;

public record AccessRules(
        String ownerId,
        List<String> editorIds,
        List<String> viewerIds
) {

    public boolean canView(AppUser user) {
        return viewerIds().contains(user.getId());
    }

    public boolean canEdit(AppUser user) {
        return editorIds().contains(user.getId());
    }

    public boolean isOwner(AppUser user) {
        return ownerId().equals(user.getId());
    }
}
