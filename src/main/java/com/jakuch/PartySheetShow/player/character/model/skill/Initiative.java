package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;

@Data
public class Initiative extends BaseSkill {
    @Override
    public String getDisplayName() {
        return "Initiative bonus";
    }
}
