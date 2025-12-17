package com.jakuch.PartySheetShow.character.controller;

import com.jakuch.PartySheetShow.character.form.CharacterForm;
import com.jakuch.PartySheetShow.character.service.CharacterService;
import com.jakuch.PartySheetShow.level.model.Level;
import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.characterClass.service.CharacterClassFetcherService;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import com.jakuch.PartySheetShow.open5e.races.service.RaceFetcherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes({"character", "chosenRace", "chosenClass"})
public class CharacterAddController {

    private CharacterService characterService;
    private CharacterClassFetcherService characterClassFetcherService;
    private RaceFetcherService raceFetcherService;

    @ModelAttribute("levels")
    public List<Level> levels() {
        return Arrays.asList(Level.values());
    }

    @ModelAttribute("classes")
    public List<CharacterClass> classes() {
        return characterClassFetcherService.fetchAllMappedData();
    }

    @ModelAttribute("races")
    public List<Race> races() {
        return raceFetcherService.fetchAllMappedData();
    }

    @GetMapping("/characterAdd")
    public ModelAndView addCharacterForm() {
        var modelAndView = new ModelAndView();
        modelAndView.addObject("character", new CharacterForm());
        modelAndView.addObject("chosenClass", new CharacterClass());
        modelAndView.addObject("chosenRace", new Race());
        modelAndView.setViewName("characterAdd");
        return modelAndView;
    }

    @PostMapping("/characterAdd/submitForm")
    public String addCharacter(@ModelAttribute("character") CharacterForm characterForm) {
        characterService.saveCharacter(characterForm);
        return "redirect:/characters";
    }

    @PostMapping("/characterAdd/addRace")
    public String addRace(Race chosenRace, Model model) {
        if (!chosenRace.getSrdKey().isEmpty()) {
            var race = raceFetcherService.fetchFullDataOfSingleRecord(chosenRace.getSrdKey());//TODO instead fetch full data (like features by level and so on)
            ((CharacterForm) model.getAttribute("character")).setRaceSrdKey(race.getSrdKey());
            model.addAttribute("chosenRace", race);
        }

        return "characterAdd";
    }

    @PostMapping("/characterAdd/addClass")
    public String addClass(CharacterClass chosenClass, Model model) {
        if (!chosenClass.getSrdKey().isEmpty()) {
            var clazz = characterClassFetcherService.fetchFullDataOfSingleRecord(chosenClass.getSrdKey());//TODO instead fetch full data (like features by level and so on)
            ((CharacterForm) model.getAttribute("character")).getCharacterClassSrdKey().add(clazz.getSrdKey());
            model.addAttribute("chosenClass", clazz);
        }

        return "characterAdd";
    }
}
