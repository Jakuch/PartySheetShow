package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tool extends BaseSkill {

    private ToolName name;

    @Override
    public String getDisplayName() {
        return name.name();
    }
}
