package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.CHARACTER_CLASS;

@Service
public class CharacterClassService extends Open5eServiceBase<Open5eClass> {

    public CharacterClassService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties,"/classes/", CHARACTER_CLASS, Open5eClass.class);
    }

    @Cacheable("classesAll")
    public List<Open5eClass> getAllClasses() {
        getAll().forEach(this::setClassSrdKeyInFeatures);
        return getAll().stream().filter(characterClass -> characterClass.getSubclass() == null).collect(Collectors.toList());
    }

    @Override
    public Optional<Open5eClass> getByKey(String key) {
        var optionalCharacterClass = super.getByKey(key);
        optionalCharacterClass.ifPresent(this::setClassSrdKeyInFeatures);
        return optionalCharacterClass;
    }

    public List<Open5eClass> getAllSubclassesForClass(String srdClassKey) {
        return getAll(Map.of("subclass_of", srdClassKey));
    }

    private void setClassSrdKeyInFeatures(Open5eClass open5eClass) {
        open5eClass.getFeatures().forEach(feature -> feature.setClassSrdKey(open5eClass.getSrdKey()));
    }
}
