package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.player.character.form.CharacterForm;
import com.jakuch.PartySheetShow.player.character.mapper.CharacterMapper;
import com.jakuch.PartySheetShow.player.character.model.AbilityName;
import com.jakuch.PartySheetShow.player.character.model.Advantage;
import com.jakuch.PartySheetShow.player.character.model.Level;
import com.jakuch.PartySheetShow.player.character.model.Proficiency;
import com.jakuch.PartySheetShow.player.character.model.skill.SkillName;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import com.jakuch.PartySheetShow.security.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@AllArgsConstructor
@RequestMapping("/characters")
public class CharacterController {

    private CharacterService characterService;
    private CharacterMapper characterMapper;
    private AppUserService appUserService;

    @GetMapping
    public String characters(Model model) {
        model.addAttribute("characters", characterService.findAllForUser(appUserService.getCurrentUser()));
        model.addAttribute("abilityNames", AbilityName.correctValues());
        return "characters";
    }

    @GetMapping("/{id}")
    public String characterSheet(@PathVariable String id, @RequestParam(defaultValue = "view") String mode, Model model) {
        populateModel(id, mode, model);

        return "characterSheet";
    }

    @PostMapping("/{id}")
    public String updateCharacter(@PathVariable String id, @ModelAttribute("editForm") CharacterForm characterForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populateModel(id, "edit", model);
            return "characterSheet";
        }

        characterService.updateCharacter(id, characterForm);
        return "redirect:/characters/" + id + "?mode=view";
    }

    private void populateModel(String id, String mode, Model model) {
        var character = characterService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var characterForm = characterMapper.toForm(character);

        model.addAttribute("character", character);
        model.addAttribute("editForm", characterForm);
        model.addAttribute("mode", mode);
        model.addAttribute("levels", Level.values());
        model.addAttribute("abilityNames", AbilityName.correctValues());
        model.addAttribute("skillNames", SkillName.values());
        model.addAttribute("proficiencies", Proficiency.values());
        model.addAttribute("advantages", Advantage.values());
    }

    @PostMapping("/{id}/delete")
    public String deleteById(@PathVariable String id) {
        characterService.deleteCharacter(id);
        return "redirect:/characters";
    }
}
