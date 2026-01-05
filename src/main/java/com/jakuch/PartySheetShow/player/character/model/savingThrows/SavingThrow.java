package com.jakuch.PartySheetShow.player.character.model.savingThrows;

import com.jakuch.PartySheetShow.open5e.attributes.model.Skill;
import com.jakuch.PartySheetShow.player.character.model.Advantage;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.open5e.attributes.model.AttributeName;

import java.util.List;
import java.util.stream.Collectors;

public class SavingThrow extends Skill {
    public SavingThrow(String name,int value) {
        setName(name);
        setValue(value);
        setProficiency(Proficiency.NONE);
        setAdvantage(Advantage.NONE);
        setModifier(AttributeName.findByName(name).getSrdKey());
    }
}