package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;

@Data
public class CustomSkill extends BaseSkill{

    private String name;

    @Override
    public String getDisplayName() {
        return this.name;
    }
}
