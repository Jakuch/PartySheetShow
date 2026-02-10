package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.dataParser.ClassProficienciesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.RaceTraitsParser;
import com.jakuch.PartySheetShow.open5e.dataParser.StartingEquipmentParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.open5e.model.*;
import com.jakuch.PartySheetShow.open5e.services.*;
import com.jakuch.PartySheetShow.player.character.model.Race;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/open5e")
public class Open5eController {

    private RaceService raceService;
    private CharacterClassService characterClassService;
    private ClassProficienciesParser classProficienciesParser;
    private AbilityService abilityService;
    private ItemService itemService;
    private StartingEquipmentParser startingEquipmentParser;

    @GetMapping("/item-sets")
    public Map<String, List<Open5eItem>> getItemSets() {
        List<Open5eItemSet> allSets = itemService.getAllSets();
        return allSets.stream().collect(Collectors.toMap(Open5eData::getSrdKey, Open5eItemSet::getItems));
    }

    @GetMapping("/tools")
    public Optional<Open5eItemSet> getOnlyTools() {
        return itemService.getSet(ItemSetsService.ItemSetType.ARTISANS_TOOLS);
    }

    @GetMapping("/instruments")
    public Optional<Open5eItemSet> getOnlyInstruments() {
        return itemService.getSet(ItemSetsService.ItemSetType.MUSICAL_INSTRUMENTS);
    }

    @GetMapping("/normal-items")
    public List<Open5eItem> getNormalItems() {
        return itemService.getAll(Map.of("is_magic_item", false));
    }

    @GetMapping("/weapons")
    public List<Open5eItem> getWeapons() {
        return itemService.getAll(Map.of(
                "is_magic_item", false,
                "is_weapon", true));
    }

    @GetMapping("/armors")
    public List<Open5eItem> getArmors() {
        return itemService.getAll(Map.of(
                        "is_magic_item", false,
                        "is_armor", true));
    }

    @GetMapping("/all-traits-desc")
    public List<String> getAllTraitsDesc() {
        return raceService.getAll().stream().flatMap(r -> r.getRaceTraits().stream()).map(Open5eData::getDescription).toList();
    }

    @GetMapping("/all-subclasses")
    public List<Open5eClass> getSubclasses(@RequestParam String srdClassKey) {
        return characterClassService.getAllSubclassesForClass(srdClassKey);
    }

    @GetMapping("/class-proficiencies")
    public Map<String, ProficiencySupportClass> getClassProficiencies() {
        return characterClassService.getAllClasses().stream()
                .collect(Collectors.toMap(Open5eData::getName, el -> new ProficiencySupportClass(classProficienciesParser.mapToClassProficiencies(el.getClassProficienciesFeature().getDescription()), el.getClassProficienciesFeature().getDescription())));
    }

    @GetMapping("/class-profiraw")
    public List<String> getRawprofi() {
        return characterClassService.getAllClasses().stream().map(el -> el.getClassProficienciesFeature().getDescription()).toList();
    }

    @GetMapping("/class-prof-types")
    public Map<String, List<String>> getFeatureTypes() {
        return characterClassService.getAllClasses().stream().collect(Collectors.toMap(Open5eData::getName, c -> c.getFeatures().stream().map(Open5eFeature::getType).toList()));
    }

    @GetMapping("/class-start-eq")
    public Map<String, String> getStargEq() {
        return characterClassService.getAllClasses().stream().collect(Collectors.toMap(Open5eData::getName, c -> c.getStartingEquipment().getDescription()));
    }

    @GetMapping("/class-start-eq-split")
    public Map<String, List<String>> getStargEqSplit() {
        return characterClassService.getAllClasses().stream().collect(Collectors.toMap(Open5eData::getName, c -> startingEquipmentParser.parseStartingEquipment(c.getStartingEquipment())));
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
