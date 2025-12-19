package com.jakuch.PartySheetShow.player.level.controller;

import com.jakuch.PartySheetShow.player.level.model.Level;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
public class LevelsController {

    @GetMapping("/levelsTable")
    public String characters(Model model) {
        model.addAttribute("levels", Arrays.asList(Level.values()));
        return "levelsTable";
    }
}
