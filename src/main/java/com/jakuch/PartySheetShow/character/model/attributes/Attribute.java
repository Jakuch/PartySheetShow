package com.jakuch.PartySheetShow.character.model.attributes;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Attribute {
    private int value;
    private int bonus;

    public Attribute(int value) {
        this.value = value;
        calculateBonus();
    }

    private void calculateBonus() {
        this.bonus = (this.value-10)/2;
    }
}
