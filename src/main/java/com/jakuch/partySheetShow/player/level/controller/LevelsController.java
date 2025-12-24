package com.jakuch.partySheetShow.player.level.controller;

import com.jakuch.partySheetShow.player.level.model.Level;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class LevelsController {

    @GetMapping("/levelsTable")
    public String characters(Model model) {
        model.addAttribute("levels", Arrays.asList(Level.values()));
        return "levelsTable";
    }
}
