package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.player.dice.DiceType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CharacterClass {
    private String srdKey;
    private String name;
    private Level level;
    private boolean isFirst;
    private DiceType hitDice;
    private List<Feature> features;
    private ClassProficiencies classProficiencies;
    private StartingEquipment startingEquipment;
}
