package com.jakuch.PartySheetShow.initiativeTracker.controller;

import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeForm;
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeTrackerForm;
import com.jakuch.PartySheetShow.initiativeTracker.service.InitiativeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.util.Comparator;

@Controller
@AllArgsConstructor
public class InitiativeTrackerController {

    private InitiativeService initiativeService;

    @GetMapping("/initiativeTracker")
    public ModelAndView get(InitiativeTrackerForm initiativeTracker, ModelAndView modelAndView) {
        modelAndView.addObject("initiativeTracker", initiativeTracker);
        modelAndView.setViewName("initiativeTracker");
        return modelAndView;
    }

    @RequestMapping(value = "/initiativeTracker", params = {"saveTracker"})
    public Model saveTracker(@ModelAttribute("initiativeTracker") InitiativeTrackerForm initiativeTracker, Model model) {
        var initiativeId = initiativeService.saveInitiativeTracker(initiativeTracker);
        model.addAttribute("initiativeId", initiativeId);
        return model;
    }

    @RequestMapping(value = "/initiativeTracker", params = {"loadTracker"})
    public Model loadTracker(@RequestParam(value = "id") String id, Model model) throws FileNotFoundException {
        var initiativeTrackerForm = initiativeService.loadInitiativeTracker(id);
        model.addAttribute("initiativeTracker", initiativeTrackerForm);
        return model;
    }

    @RequestMapping(value = "/initiativeTracker", params = {"addRow"})
    public Model addRow(InitiativeTrackerForm initiativeTracker, Model model) {
        initiativeTracker.getInitiativeList().add(new InitiativeForm());
        model.addAttribute("initiativeTracker", initiativeTracker);
        return model;
    }

    @RequestMapping(value = "/initiativeTracker", params = {"removeRow"})
    public Model removeRow(InitiativeTrackerForm initiativeTracker, Model model) {
        initiativeTracker.getInitiativeList().remove(initiativeTracker.getInitiativeList().size() - 1);
        model.addAttribute("initiativeTracker", initiativeTracker);
        return model;
    }

    @RequestMapping(value = "/initiativeTracker", params = {"sort"})
    public Model sortInitiative(InitiativeTrackerForm initiativeTracker, Model model) {
        initiativeTracker.getInitiativeList().sort(Comparator.comparing(InitiativeForm::getValue, Comparator.nullsLast(Integer::compareTo)).reversed());
        model.addAttribute("initiativeTracker", initiativeTracker);
        return model;
    }

    @GetMapping("/initiativeTracker/deleteAll")
    public String deleteAll() {
        initiativeService.deleteAll();
        return "redirect:/initiativeTracker";
    }

}
