package com.jakuch.PartySheetShow.player.character.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SavingThrow extends Skill {
    public SavingThrow(String name) {
        setName(name);
        setValue(0);
        setProficiency(Proficiency.NONE);
        setAdvantage(Advantage.NONE);
        setModifier(AbilityName.findByName(name).getSrdKey());
    }
}