package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkillChoice implements Choice {
    private SkillName choice;
    private List<SkillName> options;
}
