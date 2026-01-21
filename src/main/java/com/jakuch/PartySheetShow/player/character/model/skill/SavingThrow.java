package com.jakuch.PartySheetShow.player.character.model.skill;

import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import lombok.Data;

@Data
public class SavingThrow extends BaseSkill {

    private AbilityName abilityName;

    @Override
    public String getDisplayName() {
        return abilityName.getName() + " saving throw";
    }
}
