package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.Open5eData;
import lombok.Data;

import java.util.List;

@Data
public class Ability extends Open5eData {

    private int value;

    private int bonus;

    private List<Skill> skills;

    public String getUpperCaseName() {
        return this.name.toUpperCase();
    }

    public void calculateBonusesAndSkills(Level level) {
        calculateBonus();
        calculateSkills(level);
    }

    private void calculateBonus() {
        this.bonus = (this.value - 10) / 2;
    }

    private void calculateSkills(Level level) {
        this.skills.forEach(skill -> skill.setValueWithProficiency(this.getBonus(), level));
    }

    public void updateValue(int value, Level level) {
        this.value += value;
        calculateBonusesAndSkills(level);
    }
}
