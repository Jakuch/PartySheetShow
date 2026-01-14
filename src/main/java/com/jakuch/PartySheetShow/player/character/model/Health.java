package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Health {
    private int current;
    private int max;
    private int temporary;
    private Map<DiceType, Integer> availableHitDices;
    private Map<DiceType, Integer> hitDices;


}
