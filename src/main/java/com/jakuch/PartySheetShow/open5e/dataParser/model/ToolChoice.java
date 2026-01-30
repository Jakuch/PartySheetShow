package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.open5e.model.Open5eItem;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ToolChoice implements Choice {
    private ToolName toolChoice;
    private List<ToolName> options;
}
