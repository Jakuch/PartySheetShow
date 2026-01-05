package com.jakuch.PartySheetShow.open5e.characterClass.service;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.CHARACTER_CLASS;

@Service
public class CharacterClassService extends Open5eServiceBase<CharacterClass> {

    public CharacterClassService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties,"/classes/", CHARACTER_CLASS, CharacterClass.class);
    }

    @Cacheable("classesAll")
    public List<CharacterClass> getAllClasses() {
        return getAll().stream().filter(characterClass -> characterClass.getSubclass() == null).collect(Collectors.toList());
    }

    public List<CharacterClass> getAllSubclassesForClass(String srdClassKey) {
        return null;
    }
}
