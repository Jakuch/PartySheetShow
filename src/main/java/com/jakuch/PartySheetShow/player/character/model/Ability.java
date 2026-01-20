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
    private int bonus;
    private List<Skill> skills;

    public int getTotalValue() {
        return this.value + this.bonusValue;
    }

    private void calculateBonus() {
        this.bonus = (this.value - 10) / 2;
    }

    private void setSkillValues() {
        this.skills.forEach(skill -> skill.setValue(this.getBonus()));
    }

    public void addValue(int value) {
        this.value += value;
        calculateBonus();
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
