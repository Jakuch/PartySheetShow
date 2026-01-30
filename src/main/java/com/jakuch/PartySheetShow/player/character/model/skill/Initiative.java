package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Initiative extends BaseSkill {
    @Override
    public String getDisplayName() {
        return "Initiative bonus";
    }
}
