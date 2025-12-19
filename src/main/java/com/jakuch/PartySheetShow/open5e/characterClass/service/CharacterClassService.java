package com.jakuch.PartySheetShow.open5e.characterClass.service;

import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterClassService extends Open5eServiceBase<CharacterClass> {

    private final static ParameterizedTypeReference<Open5eResponse<CharacterClass>> TYPE = new ParameterizedTypeReference<>() {
    };

    public CharacterClassService(Open5eClient open5eClient) {
        super(open5eClient, "/classes/", TYPE, CharacterClass.class);
    }

    @Cacheable("classesAll")
    public List<CharacterClass> getAllClasses() {
        return getAll().stream().filter(characterClass -> characterClass.getSubclass() == null).collect(Collectors.toList());
    }

    public List<CharacterClass> getAllSubclassesForClass(String srdClassKey) {
        return null;
    }
}
