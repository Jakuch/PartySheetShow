package com.jakuch.PartySheetShow.security.model;

import lombok.Builder;

import java.util.List;

@Builder
public record AccessRules(
        String ownerId,
        List<String> editorIds,
        List<String> viewerIds
) {

    public boolean canView(AppUser user) {
        if (isOwner(user)) return true;
        return viewerIds().contains(user.getId());
    }

    public boolean canEdit(AppUser user) {
        if (isOwner(user)) return true;
        return editorIds().contains(user.getId());
    }

    public boolean isOwner(AppUser user) {
        return user != null && ownerId != null && ownerId.equals(user.getId());
    }
}
