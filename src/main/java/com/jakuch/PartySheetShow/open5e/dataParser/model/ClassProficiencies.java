package com.jakuch.PartySheetShow.open5e.dataParser.model;


import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;

import java.util.List;

@Builder
public record ClassProficiencies(
        List<String> armor,
        List<String> weapons,
        List<ToolName> tools,
        List<ToolChoice> toolChoices,
        List<AbilityName> savingThrows,
        List<SkillChoice> skillChoices
) {
}
