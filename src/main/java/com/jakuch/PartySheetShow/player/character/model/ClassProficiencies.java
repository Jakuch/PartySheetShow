package com.jakuch.PartySheetShow.player.character.model;


import java.util.List;

public record ClassProficiencies(
        List<String> armor,
        List<String> weapons,
        List<String> tools,
        List<AttributeName> savingThrows,
        List<SkillName> skillProficiencies,
        int skillProficienciesChoseCount
) {}
