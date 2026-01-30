package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Skill extends BaseSkill {

    private SkillName name;

    @Override
    public String getDisplayName() {
        return name.getDisplayName();
    }
}
