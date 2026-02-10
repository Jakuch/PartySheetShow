package com.jakuch.PartySheetShow.player.character.model.skill;

import com.jakuch.PartySheetShow.player.character.model.Advantage;
import com.jakuch.PartySheetShow.player.character.model.Level;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import lombok.Data;

@Data
public abstract class BaseSkill {
    private int value;
    private int bonusValue;
    private Proficiency proficiency = Proficiency.NONE;
    private Advantage advantage = Advantage.NONE;

    public abstract String getDisplayName();

    public int getTotalValue(Level level) {
        return this.value + this.bonusValue + calculateProficiencyBonus(level);
    }

    private int calculateProficiencyBonus(Level level) {
        return Proficiency.calculateProficiencyBonus(this.proficiency, level);
    }

    public void setProficient() {
        this.proficiency = Proficiency.FULL;
    }

    public void setExpert() {
        this.proficiency = Proficiency.EXPERTISE;
    }
}
