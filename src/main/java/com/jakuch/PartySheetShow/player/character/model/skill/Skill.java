package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Skill extends BaseSkill {

    private SkillName skillName;

    @Override
    public String getDisplayName() {
        return skillName.getDisplayName();
    }
}
