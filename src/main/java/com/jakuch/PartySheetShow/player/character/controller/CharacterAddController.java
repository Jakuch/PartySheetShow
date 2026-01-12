package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.open5e.services.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.services.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.model.*;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        characterForm.setAttributes(Arrays.stream(AttributeName.values())
                .collect(Collectors.toMap(
                        attributeName -> attributeName,
                        attr -> 0)
                ));

        return characterForm;
    }

    @ModelAttribute("levels")
    public List<Level> levels() {
        return Arrays.asList(Level.values());
    }

    @ModelAttribute("classes")
    public List<CharacterClass> classes() {
        return characterClassService.getAllClasses();
    }

    @ModelAttribute("selectedClassesFeatures")
    public List<CharacterClass> selectedClassesFeatures() {
        return new ArrayList<>();
    }

    @ModelAttribute("races")
    public List<Race> races() {
        return raceService.getAll();
    }

    @ModelAttribute("selectedRaceTraits")
    public List<RaceTrait> selectedRaceTraits() {
        return new ArrayList<>();
    }

    @GetMapping
    public String addCharacterForm(Model model) {
        model.addAttribute("attributeNames", AttributeName.correctValues());

        return "characterAdd";
    }

    @PostMapping(params = {"addClass"})
    public String addClass(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("classes") List<CharacterClass> classes, @ModelAttribute("selectedClassesFeatures") List<CharacterClass> selectedClassesFeatures) {
        var classKey = characterForm.getChosenCharacterClassKey();
        if(!StringUtils.isEmptyOrWhitespace(classKey) && characterForm.getClasses().get(classKey) == null) {
            characterClassService.getByKey(classKey).ifPresent(selectedClassesFeatures::add);
            characterService.addClassToForm(characterForm, classes);
        }
        characterForm.setChosenCharacterClassKey(null);

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteClass"})
    public String deleteClass(@ModelAttribute("character") CharacterForm characterForm, @RequestParam String classKey, @ModelAttribute("selectedClassesFeatures") List<CharacterClass> selectedClassesFeatures) {
        selectedClassesFeatures.removeIf(characterClass -> classKey.equalsIgnoreCase(characterClass.getSrdKey()));
        characterService.deleteClassFromForm(characterForm, classKey);
        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"addRace"})
    public String addRace(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("races") List<Race> races, @ModelAttribute("selectedRaceTraits") List<RaceTrait> selectedRaceTraits) {
        var raceKey = characterForm.getChosenRaceKey();
        if (!StringUtils.isEmptyOrWhitespace(raceKey) && characterForm.getRace() == null) {
            raceService.getByKey(raceKey).ifPresent(race -> {
                selectedRaceTraits.addAll(race.getRaceTraits());
            });
            characterService.addRaceToForm(characterForm, races);
        }
        characterForm.setChosenRaceKey(null);

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteRace"})
    public String deleteRace(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("selectedRaceTraits") List<RaceTrait> selectedRaceTraits) {
        if (characterForm.getRace() != null) {
            selectedRaceTraits.clear();
            characterService.deleteRaceFromForm(characterForm);
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
