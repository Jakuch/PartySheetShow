package com.jakuch.PartySheetShow.open5e;

import com.jakuch.PartySheetShow.open5e.dataParser.AbilitiesParser;
import com.jakuch.PartySheetShow.open5e.dataParser.model.AbilityBonuses;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.open5e.dataParser.model.ClassProficiencies;
import com.jakuch.PartySheetShow.player.character.model.Race;
import com.jakuch.PartySheetShow.player.character.model.RaceTrait;
import com.jakuch.PartySheetShow.open5e.dataParser.ProficienciesParser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private AbilitiesParser abilitiesParser;

    @GetMapping("/all-traits")
    public List<RaceTrait> getAllRaceTraits() {
        return raceService.getAllRaceTraits();
    }

    @GetMapping("/asi-traits")
    public List<RaceTrait> getAbilityImprovementTraits() {
        return raceService.getAllAbilityImprovements();
    }

    @GetMapping("/asi-traits-mapped")
    public List<AbilityBonuses> getAbilityBonuses() {
        return raceService.getAllAbilityImprovements().stream().map(Open5eData::getDescription).map(el -> abilitiesParser.parse(el)).toList();
    }

    @GetMapping("/class-proficiencies")
    public Map<String, ProficiencySupportClass> getClassProficiencies() {
        return characterClassService.getAllClasses().stream()
                .collect(Collectors.toMap(Open5eData::getName, el -> new ProficiencySupportClass(proficienciesParser.mapToClassProficiencies(el.getClassProficienciesFeature().description), el.getClassProficienciesFeature().description)));
    }

    public record ProficiencySupportClass(ClassProficiencies classProficiencies, String rawFeatureDescription) { }
}
