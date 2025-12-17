package com.jakuch.PartySheetShow.character.model.savingThrows;

import com.jakuch.PartySheetShow.character.model.Advantage;
import com.jakuch.PartySheetShow.character.model.Proficiency;
import com.jakuch.PartySheetShow.character.model.attributes.AttributeName;
import com.jakuch.PartySheetShow.character.model.skills.Skill;

import java.util.List;
import java.util.stream.Collectors;

public class SavingThrow extends Skill {
    public SavingThrow(String name) {
        setName(name);
        setValue(0);
        setProficiency(Proficiency.NONE);
        setAdvantage(Advantage.NONE);
        setModifier(AttributeName.findByName(trimNameIfNeeded(name)));
    }

    private String trimNameIfNeeded(String name) {
        int index = name.indexOf(" ");
        if (index != -1) {
            return name.substring(0, index);
        }
        return name;
    }

    public static List<SavingThrow> initializeSavingThrows() {
        return AttributeName.correctValues()
                .stream()
                .map(attributeName -> new SavingThrow(attributeName.getNormalName() + " saving throw"))
                .collect(Collectors.toList());
    }
}