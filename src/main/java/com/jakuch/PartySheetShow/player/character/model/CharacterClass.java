package com.jakuch.PartySheetShow.player.character.model;

import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import lombok.Data;

import java.util.List;

@Data
public class CharacterClass {
    private String srdKey;
    private String name;
    private Level level;
    private boolean isFirst;
    private List<Features> features;
    private ClassProficiencies classProficiencies;

}
