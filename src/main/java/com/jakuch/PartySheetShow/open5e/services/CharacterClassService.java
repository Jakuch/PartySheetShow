package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.dataParser.ProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.player.character.model.CharacterClass;
import com.jakuch.PartySheetShow.player.character.model.Feature;
import com.jakuch.PartySheetShow.player.character.model.FeatureType;
import com.jakuch.PartySheetShow.player.character.model.Level;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.CHARACTER_CLASS;

@Service
public class CharacterClassService extends Open5eServiceBase<Open5eClass> {

    private ProficienciesParser proficienciesParser;

    public CharacterClassService(Open5eClient open5eClient, Open5eProperties open5eProperties, ProficienciesParser proficienciesParser) {
        super(open5eClient, open5eProperties, "/classes/", CHARACTER_CLASS, Open5eClass.class);
        this.proficienciesParser = proficienciesParser;
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

    public Optional<CharacterClass> getMappedByKey(String key) {
        return getByKey(key).map(open5eClass ->
                CharacterClass.builder()
                        .srdKey(open5eClass.getSrdKey())
                        .name(open5eClass.getName())
                        .hitDice(open5eClass.getHitDice())
                        .features(mapFeatures(open5eClass))
                        .classProficiencies(mapClassProficiencies(open5eClass))
                        .build());

    }

    public List<Open5eClass> getAllSubclassesForClass(String srdClassKey) {
        return getAll(Map.of("subclass_of", srdClassKey));
    }

    private void setClassSrdKeyInFeatures(Open5eClass open5eClass) {
        open5eClass.getFeatures().forEach(feature -> feature.setClassSrdKey(open5eClass.getSrdKey()));
    }

    private List<Feature> mapFeatures(Open5eClass open5eClass) {
        return open5eClass.getFeatures().stream()
                .flatMap(feature -> feature
                        .getGainedAtLevels().stream()
                        .map(lvl ->
                                Feature.builder()
                                        .srdKey(feature.getSrdKey())
                                        .classSrdKey(feature.getClassSrdKey())
                                        .type(FeatureType.valueOf(feature.getType()))
                                        .name(feature.getName())
                                        .description(feature.getDescription())
                                        .gainedAtLevel(Level.findByNumericValue(lvl))
                                        .build()))
                .toList();
    }

    private ClassProficiencies mapClassProficiencies(Open5eClass open5eClass) {
        return proficienciesParser.mapToClassProficiencies(open5eClass.getClassProficienciesFeature().getDescription());
    }
}
