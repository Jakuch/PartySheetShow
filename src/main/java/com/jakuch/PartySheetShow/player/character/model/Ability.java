package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.player.character.model.skill.Skill;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Ability {
    private AbilityName name;
    private int value;
    private int bonusValue;
    private List<Skill> skills;

    public int getTotalValue() {
        return this.value + this.bonusValue;
    }

    public int getAbilityBonus() {
        return (getTotalValue() - 10) / 2;
    }

    private void setSkillValues() {
        this.skills.forEach(skill -> skill.setValue(this.getAbilityBonus()));
    }

    public void addBonusValue(int bonusValue) {
        this.bonusValue += bonusValue;
    }

    public static class AbilityBuilder {
        private int value;
        private int bonus;
        private List<Skill> skills;

        public AbilityBuilder setSkillValues() {
            this.bonus = (this.value - 10) / 2;
            this.skills.forEach(skill -> skill.setValue(this.bonus));
            return this;
        }

    }
}
