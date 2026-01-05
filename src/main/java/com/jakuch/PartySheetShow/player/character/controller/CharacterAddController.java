package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.characterClass.service.CharacterClassService;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import com.jakuch.PartySheetShow.open5e.races.service.RaceService;
import com.jakuch.PartySheetShow.player.character.form.CharacterClassForm;
import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.form.RaceForm;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import com.jakuch.PartySheetShow.player.level.model.Level;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/characterAdd")
@SessionAttributes({"character", "classes", "races"})
public class CharacterAddController {

    private CharacterService characterService;
    private CharacterClassService characterClassService;
    private RaceService raceService;

    @ModelAttribute("character")
    public CharacterForm characterForm() {
        return new CharacterForm();
    }
    @ModelAttribute("levels")
    public List<Level> levels() {
        return Arrays.asList(Level.values());
    }

    @ModelAttribute("classes")
    public List<CharacterClass> classes() {
        return characterClassService.getAllClasses();
    }

    @ModelAttribute("races")
    public List<Race> races() {
        return raceService.getAll();
    }

    @GetMapping
    public String addCharacterForm(Model model) {
        return "characterAdd";
    }

    @PostMapping(params = {"addClass"})
    public String addClass(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("classes") List<CharacterClass> classes) {
        var classKey = characterForm.getChosenCharacterClassKey();
        var selectedClassesKeys = characterForm.getCharacterClasses().stream().map(CharacterClassForm::getKey).toList();
        if(!StringUtils.isEmptyOrWhitespace(classKey) && !selectedClassesKeys.contains(classKey)) {
            var characterClass = classes.stream().filter(c -> classKey.equals(c.getSrdKey())).findFirst();
            characterClass.ifPresent(c -> {
                if(characterForm.getCharacterClasses() == null) {
                    characterForm.setCharacterClasses(new ArrayList<>());
                }
                characterForm.getCharacterClasses().add(new CharacterClassForm(
                        c.getSrdKey(),
                        c.getName()
                ));
            });
        }
        characterForm.setChosenCharacterClassKey(null);

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteClass"})
    public String deleteClass(@ModelAttribute("character") CharacterForm characterForm, @RequestParam String classKey) {
        var classToDelete = characterForm.getCharacterClasses().stream().filter(c -> classKey.equals(c.getKey())).findFirst();
        classToDelete.ifPresent(c -> characterForm.getCharacterClasses().remove(c));

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"addRace"})
    public String addRace(@ModelAttribute("character") CharacterForm characterForm, @ModelAttribute("races") List<Race> races) {
        var raceKey = characterForm.getChosenRaceKey();
        if(!StringUtils.isEmptyOrWhitespace(raceKey) && characterForm.getRace() == null) {
            var race = races.stream().filter(r -> raceKey.equals(r.getSrdKey())).findFirst();
            race.ifPresent(r -> characterForm.setRace(new RaceForm(
                    r.getSrdKey(),
                    r.getName()
            )));
        }
        characterForm.setChosenRaceKey(null);

        return "redirect:/characterAdd";
    }

    @PostMapping(params = {"deleteRace"})
    public String deleteRace(@ModelAttribute("character") CharacterForm characterForm) {
        if(characterForm.getRace() != null) {
            characterForm.setRace(null);
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
