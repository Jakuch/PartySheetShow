package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
public class SkillOrToolChoice implements Choice<SkillOrToolChoice.ToolsSkills> {
    private ToolName toolChoice;
    private SkillName skillChoice;
    private List<SkillName> skillOptions;
    private List<ToolName> toolsOptions;

    @Override
    public void setChoice(ToolsSkills choice) {
        SkillName.fromSrdKey(choice.getSrdKey())
                .ifPresent(skillName -> this.skillChoice = skillName);
        ToolName.fromSrdKey(choice.getSrdKey())
                .ifPresent(toolName -> this.toolChoice = toolName);
    }

    @Override
    public ToolsSkills getChoice() {
        return toolChoice != null ? getToolsSkills(toolChoice.getDisplayName(), toolChoice.getSrdKey()) :
                skillChoice != null ? getToolsSkills(skillChoice.getDisplayName(), skillChoice.getSrdKey()) : null;
    }

    @Override
    public List<ToolsSkills> getOptions() {
        var tools = toolsOptions.stream().map(toolName -> getToolsSkills(toolName.getDisplayName(), toolName.getSrdKey())).toList();

        var skills = skillOptions.stream().map(skillName -> getToolsSkills(skillName.getDisplayName(), skillName.getSrdKey())).toList();

        var result = new ArrayList<ToolsSkills>();
        result.addAll(skills);
        result.addAll(tools);

        return result;
    }

    private ToolsSkills getToolsSkills(String toolChoice, String toolChoice1) {
        return ToolsSkills.builder()
                .name(toolChoice)
                .srdKey(toolChoice1).build();
    }

    @Data
    @Builder
    public static class ToolsSkills {
        private String name;
        private String srdKey;
    }
}
