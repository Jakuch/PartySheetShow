package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class ToolChoice implements Choice<ToolName> {
    private ToolName toolChoice;
    private List<ToolName> options;

    @Override
    public void setChoice(ToolName choice) {
        this.toolChoice = choice;
    }

    @Override
    public ToolName getChoice() {
        return toolChoice;
    }

    @Override
    public List<ToolName> getOptions() {
        return options;
    }
}
