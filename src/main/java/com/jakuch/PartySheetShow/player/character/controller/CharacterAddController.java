package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.open5e.model.Open5eClass;
import com.jakuch.PartySheetShow.open5e.model.Open5eFeature;
import com.jakuch.PartySheetShow.open5e.model.Open5eRace;
import com.jakuch.PartySheetShow.open5e.model.Open5eRaceTrait;
import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.Level;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/characterAdd")
@SessionAttributes({"character", "classes", "races", "selectedClassesFeatures", "selectedRaceTraits"})
public class CharacterAddController {

    private CharacterService characterService;
    private CharacterClassService characterClassService;
    private RaceService raceService;


    @ModelAttribute("character")
    public CharacterForm characterForm() {
        var characterForm = new CharacterForm();
        characterForm.setAbilities(Arrays.stream(AbilityName.values())
                .collect(Collectors.toMap(
                        abilityName -> abilityName,
                        attr -> 0)
                ));

        return characterForm;
    }

    @ModelAttribute("selectedClassesFeatures")
    public List<Open5eFeature> classFeatures() {
        return new ArrayList<>();
    }

    @ModelAttribute("selectedRaceTraits")
    public List<Open5eRaceTrait> raceTraits() {
        return new ArrayList<>();
    }

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("levels", Arrays.asList(Level.values()));
        model.addAttribute("abilityNames", AbilityName.correctValues());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futureClasses = executor.submit(() -> characterClassService.getAllMainClasses());
            var futureRaces = executor.submit(() -> raceService.getAll());

            model.addAttribute("classes", futureClasses.get());
            model.addAttribute("races", futureRaces.get());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public String addCharacterForm() {
        return "characterAdd";
    }

    @PostMapping(params = {"addClass"})
    public String addClass(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("classes") List<Open5eClass> classes, @ModelAttribute("selectedClassesFeatures") List<Open5eFeature> selectedClassesFeatures) {
        var classKey = characterForm.getChosenCharacterClassKey();
        if (!StringUtils.isEmptyOrWhitespace(classKey) && characterForm.getClasses().get(classKey) == null) {
            characterClassService.getByKey(classKey).ifPresent(characterClass -> {
                selectedClassesFeatures.addAll(characterClass.getFeatures());
                selectedClassesFeatures.sort(Comparator.comparingInt(Open5eFeature::getLowestGainedAtLevel));
            });
            characterService.addClassToForm(characterForm, classes);
        }

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteClass"})
    public String deleteClass(@ModelAttribute("character") CharacterForm characterForm, @RequestParam String classKey, @ModelAttribute("selectedClassesFeatures") List<Open5eFeature> selectedClassesFeatures) {
        selectedClassesFeatures.removeIf(feature -> classKey.equalsIgnoreCase(feature.getClassSrdKey()));
        characterService.deleteClassFromForm(characterForm, classKey);
        characterForm.setChosenCharacterClassKey(null);
        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"addRace"})
    public String addRace(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("races") List<Open5eRace> races, @ModelAttribute("selectedRaceTraits") List<Open5eRaceTrait> selectedRaceTraits) {
        var raceKey = characterForm.getChosenRaceKey();
        if (!StringUtils.isEmptyOrWhitespace(raceKey) && characterForm.getRace() == null) {
            raceService.getByKey(raceKey).ifPresent(race -> selectedRaceTraits.addAll(race.getRaceTraits()));
            characterService.addRaceToForm(characterForm, races);
        }

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteRace"})
    public String deleteRace(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("selectedRaceTraits") List<Open5eRaceTrait> selectedRaceTraits) {
        if (characterForm.getRace() != null) {
            selectedRaceTraits.clear();
            characterService.deleteRaceFromForm(characterForm);
            characterForm.setChosenRaceKey(null);
        }

        return "redirect:/characterAdd";
    }

    @PostMapping("/submitForm")
    public String addCharacter(@ModelAttribute("character") CharacterForm characterForm, SessionStatus sessionStatus) {
        characterService.saveCharacter(characterForm);
        sessionStatus.isComplete();
        return "redirect:/characters";
    }
}
