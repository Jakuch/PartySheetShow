package com.jakuch.PartySheetShow.open5e.dataParser.model;

import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.player.character.model.skill.ToolName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolFixedBonus implements FixedBonus {
    private ToolName toolName;
    private Proficiency proficiency;
}
