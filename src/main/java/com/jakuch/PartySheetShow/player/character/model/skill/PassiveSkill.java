package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;

@Data
public class PassiveSkill extends Skill {
    @Override
    public String getDisplayName() {
        return "Passive " + getSkillName().getDisplayName();
    }
}
