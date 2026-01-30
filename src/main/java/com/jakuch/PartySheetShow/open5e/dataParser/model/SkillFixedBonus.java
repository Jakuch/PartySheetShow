package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillFixedBonus implements FixedBonus {
    private SkillName skillName;
    private Proficiency proficiency;
}
