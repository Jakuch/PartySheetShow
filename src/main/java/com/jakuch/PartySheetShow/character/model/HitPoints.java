package com.jakuch.PartySheetShow.character.model;

import com.jakuch.PartySheetShow.dice.model.DiceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HitPoints {
    private int currentHp;
    private int maxHp;
    private int temporaryHp;
    private Map<DiceType, Integer> hitDices;
}
