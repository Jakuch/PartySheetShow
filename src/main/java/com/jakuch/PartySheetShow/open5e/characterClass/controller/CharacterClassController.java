package com.jakuch.PartySheetShow.open5e.characterClass.controller;

import com.jakuch.PartySheetShow.open5e.characterClass.model.CharacterClass;
import com.jakuch.PartySheetShow.open5e.characterClass.service.CharacterClassService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CharacterClassController {

    private CharacterClassService characterClassService;

    @GetMapping("/classes")
    public List<CharacterClass> getAllMainClasses() {
        return characterClassService.getAllClasses();
    }
}
