package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkillOrToolChoice implements Choice{
    private String choice;
    private List<SkillName> skillOptions;
    private List<ToolName> toolsOptions;
}
