package com.jakuch.partySheetShow.player.character.model.attributes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

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
        this.bonus = (this.value - 10) / 2;
    }

    public static Map<String, Attribute> initializeDefaultAttributes() {
        return AttributeName.correctValues().stream()
                .collect(Collectors.toMap(Enum::name, attributeName -> new Attribute(0)));
    }
}
