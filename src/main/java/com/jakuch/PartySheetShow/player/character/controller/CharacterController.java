package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.open5e.attributes.model.AttributeName;
import com.jakuch.PartySheetShow.open5e.attributes.model.SkillName;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@AllArgsConstructor
public class CharacterController {

    private CharacterService characterService;

    @GetMapping("/characters")
    public String characters(Model model) {
        model.addAttribute("characters", characterService.findAll());
        model.addAttribute("attributeNames", AttributeName.correctValues());
        return "characters";
    }

    @RequestMapping(value = "/characters", params = {"characterSheet"})
    public String characterSheet(@RequestParam String id, Model model) {
        var character = characterService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("character", character);
        model.addAttribute("attributeNames", AttributeName.correctValues());
        model.addAttribute("skillNames", SkillName.values());
        return "characterSheet";
    }

    @RequestMapping(value = "/characters", params = {"delete"})
    public String deleteById(@RequestParam String id) {
        characterService.deleteCharacter(id);
        return "redirect:/characters";
    }
}
