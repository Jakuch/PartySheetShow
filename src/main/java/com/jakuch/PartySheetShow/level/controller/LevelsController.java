package com.jakuch.PartySheetShow.level.controller;

import com.jakuch.PartySheetShow.level.model.Level;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
public class LevelsController {

    @ModelAttribute("levels")
    public List<Level> levels() {
        return Arrays.asList(Level.values());
    }

    @GetMapping("/levelsTable")
    public String characters() {
        return "levelsTable";
    }
}
