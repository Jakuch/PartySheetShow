package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.dataParser.ClassProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.open5e.model.Open5eFeature;
import com.jakuch.PartySheetShow.open5e.model.Open5eFeatureDataTable;
import com.jakuch.PartySheetShow.player.character.model.CharacterClass;
import com.jakuch.PartySheetShow.player.character.model.Feature;
import com.jakuch.PartySheetShow.player.character.model.FeatureType;
import com.jakuch.PartySheetShow.player.character.model.Level;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.CHARACTER_CLASS;

@Service
public class CharacterClassService extends Open5eServiceBase<Open5eClass> {

    private final ClassProficienciesParser classProficienciesParser;

    public CharacterClassService(Open5eClient open5eClient, Open5eProperties open5eProperties, ClassProficienciesParser classProficienciesParser) {
        super(open5eClient, open5eProperties, "/classes/", CHARACTER_CLASS, Open5eClass.class);
        this.classProficienciesParser = classProficienciesParser;
    }

    @Cacheable("classesAll")
    public List<Open5eClass> getAllClasses() {
        getAll().forEach(this::setClassSrdKeyInFeatures);
        return getAll().stream().filter(characterClass -> characterClass.getSubclass() == null).collect(Collectors.toList());
    }

    @Cacheable("classesMainAll")
    public List<Open5eClass> getAllMainClasses() {
        return getAll(Map.of("is_subclass", false));
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
        return open5eClass.getFeatures().stream().flatMap(open5eFeature -> {
                    var featureList = new ArrayList<Feature>();

                    if (open5eFeature.getGainedAtLevels().isEmpty()) {
                        featureList.add(mapFeature(open5eFeature, Level.FIRST));
                    } else {
                        featureList.addAll(open5eFeature.getGainedAtLevels().stream()
                                .map(lvl -> mapFeature(open5eFeature, Level.findByNumericValue(lvl)))
                                .toList());
                    }
                    return featureList.stream();
                })
                .toList();
    }

    private Feature mapFeature(Open5eFeature open5eFeature, Level level) {
        return Feature.builder()
                .srdKey(open5eFeature.getSrdKey())
                .classSrdKey(open5eFeature.getClassSrdKey())
                .type(FeatureType.valueOf(open5eFeature.getType()))
                .name(open5eFeature.getName())
                .description(open5eFeature.getDescription())
                .gainedAtLevel(level)
                .improvedWithLevel(mapImprovedWithLevel(open5eFeature.getData()))
                .build();
    }

    private Map<Level, List<String>> mapImprovedWithLevel(List<Open5eFeatureDataTable> dataTable) {
        return dataTable.stream()
                .collect(Collectors.groupingBy(
                        data -> Level.findByNumericValue(data.getLevel()),
                        HashMap::new,
                        Collectors.mapping(
                                Open5eFeatureDataTable::getValue,
                                Collectors.toList()
                        )
                ));
    }

    private ClassProficiencies mapClassProficiencies(Open5eClass open5eClass) {
        return classProficienciesParser.mapToClassProficiencies(open5eClass.getClassProficienciesFeature().getDescription());
    }
}
