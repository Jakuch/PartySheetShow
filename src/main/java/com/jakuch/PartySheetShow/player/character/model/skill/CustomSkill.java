package com.jakuch.PartySheetShow.player.character.model.skill;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomSkill extends BaseSkill {

    private String name;

    @Override
    public String getDisplayName() {
        return this.name;
    }
}
