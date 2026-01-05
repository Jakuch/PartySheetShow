package com.jakuch.PartySheetShow.open5e.spells.controller;

import com.jakuch.PartySheetShow.open5e.spells.model.Spell;
import com.jakuch.PartySheetShow.open5e.spells.service.SpellsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spells")
@AllArgsConstructor
public class SpellsController {

    private SpellsService spellsService;

    @GetMapping
    public List<Spell> fetchSpells() {
        return spellsService.getAll();
    }
}
