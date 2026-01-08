package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.player.character.model.AttributeName;
import com.jakuch.PartySheetShow.player.character.model.SkillName;
import com.jakuch.PartySheetShow.player.character.service.CharacterMapper;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import com.jakuch.PartySheetShow.security.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@AllArgsConstructor
public class CharacterController {

    private CharacterService characterService;
    private CharacterMapper characterMapper;
    private AppUserService appUserService;

    @GetMapping("/characters")
    public String characters(Model model) {
        model.addAttribute("characters", characterService.findAllForUser(appUserService.getCurrentUser()));
        model.addAttribute("attributeNames", AttributeName.correctValues());
        return "characters";
    }

    @GetMapping("/characters/{id}")
    public String characterSheet1(@PathVariable String id, @RequestParam(defaultValue = "view") String mode, Model model) {
        var character = characterService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var characterForm = characterMapper.toForm(character);

        model.addAttribute("character", character);
        model.addAttribute("characterForm", characterForm);
        model.addAttribute("mode", mode);
        model.addAttribute("attributeNames", AttributeName.correctValues());
        model.addAttribute("skillNames", SkillName.values());
        return "characterSheet";
    }

    @PostMapping("/characters/{id}/delete")
    public String deleteById(@PathVariable String id) {
        characterService.deleteCharacter(id);
        return "redirect:/characters";
    }
}
