package com.jakuch.PartySheetShow.open5e.dataParser.model;


import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.SkillName;

import java.util.List;

public record ClassProficiencies(
        List<String> armor,
        List<String> weapons,
        List<String> tools,
        List<AbilityName> savingThrows,
        List<SkillName> skillProficiencies,
        int skillProficienciesChoseCount
) {}
