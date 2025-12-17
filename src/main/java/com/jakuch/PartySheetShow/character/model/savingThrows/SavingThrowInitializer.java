package com.jakuch.PartySheetShow.character.model.savingThrows;

import com.jakuch.PartySheetShow.character.model.attributes.AttributeName;

import java.util.List;
import java.util.stream.Collectors;

public class SavingThrowInitializer {

    public static List<SavingThrow> initializeSavingThrows() {
        return AttributeName.correctValues()
                .stream()
                .map(attributeName -> new SavingThrow(attributeName.getNormalName()+ " saving throw"))
                .collect(Collectors.toList());
    }
}
