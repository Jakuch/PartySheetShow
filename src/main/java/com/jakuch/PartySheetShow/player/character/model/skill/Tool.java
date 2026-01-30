package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tool extends BaseSkill {

    private ToolName toolName;

    @Override
    public String getDisplayName() {
        return toolName.name();
    }
}
