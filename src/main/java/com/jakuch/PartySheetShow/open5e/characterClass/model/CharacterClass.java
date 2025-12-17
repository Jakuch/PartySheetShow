package com.jakuch.PartySheetShow.open5e.characterClass.model;

import com.jakuch.PartySheetShow.dice.model.DiceType;
import com.jakuch.PartySheetShow.level.model.Level;
import com.jakuch.PartySheetShow.open5e.SrdData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacterClass extends SrdData {

    private Level level;
    private String subclassSrdKey;
    private DiceType hitDice;

    public CharacterClass(String name, String srdKey) {
        this.name = name;
        this.srdKey = srdKey;
        this.level = Level.FIRST;
    }
}
