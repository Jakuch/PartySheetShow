package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.dataParser.ProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.model.*;
import com.jakuch.PartySheetShow.open5e.services.AbilityService;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.model.Race;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/open5e")
public class Open5eController {

    private RaceService raceService;
    private CharacterClassService characterClassService;
    private ProficienciesParser proficienciesParser;
    private AbilityService abilityService;

    @GetMapping("/single-race")
    public Open5eRace getRace(@RequestParam String srdKey) {
        return raceService.getByKey(srdKey).orElse(null);
    }

    @GetMapping("/single-race-mapped")
    public Race getRaceMapped(@RequestParam String srdKey) {
        return raceService.getMappedRaceByKey(srdKey).orElse(null);
    }

    @GetMapping("/all-traits")
    public List<TraitsSupportClass> getAllTraits() {
        return raceService.getAll().stream().map(r -> new TraitsSupportClass(r.getName(), r.getRaceTraits())).toList();
    }

    @GetMapping("/all-subclasses")
    public List<Open5eClass> getSubclasses(@RequestParam String srdClassKey) {
        return characterClassService.getAllSubclassesForClass(srdClassKey);
    }

    @GetMapping("/class-proficiencies")
    public Map<String, ProficiencySupportClass> getClassProficiencies() {
        return characterClassService.getAllClasses().stream()
                .collect(Collectors.toMap(Open5eData::getName, el -> new ProficiencySupportClass(proficienciesParser.mapToClassProficiencies(el.getClassProficienciesFeature().getDescription()), el.getClassProficienciesFeature().getDescription())));
    }

    @GetMapping("/class-features")
    public Map<String, ClassFeaturesSupportClass> getAllClassFeatures() {
        return characterClassService.getAllClasses().stream()
                .collect(Collectors.toMap(Open5eData::getName, el -> new ClassFeaturesSupportClass(el.getFeatures())));
    }

    @GetMapping("/features-with-data-table")
    public List<Open5eFeature> getWithDataTable() {
        return characterClassService.getAll().stream()
                .flatMap(c -> c.getFeatures().stream())
                .filter(feature -> !feature.getData().isEmpty())
                .toList();
    }

    @GetMapping("/abilities")
    public List<Open5eAbility> getAbilities() {
        return abilityService.getAll();
    }

    public record ProficiencySupportClass(ClassProficiencies classProficiencies, String rawFeatureDescription) {
    }

    public record ClassFeaturesSupportClass(List<Open5eFeature> features) {
    }

    public record TraitsSupportClass(String race, List<Open5eRaceTrait> traits) {};
}
