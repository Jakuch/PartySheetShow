package com.jakuch.PartySheetShow.player.character.controller;

import com.jakuch.PartySheetShow.player.character.model.Character;
import com.jakuch.PartySheetShow.player.character.model.attributes.AttributeName;
import com.jakuch.PartySheetShow.player.character.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class CharacterController {

    private CharacterService characterService;

    @ModelAttribute("characters")
    public List<Character> findAll() {
        return characterService.findAll();
    }

    @ModelAttribute("attributeNames")
    public List<AttributeName> attributes() {
        return AttributeName.correctValues();
    }

    @GetMapping("/characters")
    public String characters() {
        return "characters";
    }

    @RequestMapping(value = "/characters", params = {"characterSheet"})
    public ModelAndView characterSheet(@RequestParam String id) {
        var character = characterService.findById(id);
        var modelAndView = new ModelAndView();
        modelAndView.addObject("character", character);
        modelAndView.setViewName("characterSheet");
        return modelAndView;
    }

    @RequestMapping(value = "/characters", params = {"delete"})
    public String deleteById(@RequestParam String id) {
        characterService.deleteCharacter(id);
        return "redirect:/characters";
    }

    @RequestMapping(value = "/characters/deleteAll")
    public String deleteAll() {
        characterService.deleteAll();
        return "redirect:/characters";
    }
}
