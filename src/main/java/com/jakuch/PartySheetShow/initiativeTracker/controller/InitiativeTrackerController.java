package com.jakuch.PartySheetShow.initiativeTracker.controller;

import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeForm;
import com.jakuch.PartySheetShow.initiativeTracker.form.InitiativeTrackerForm;
import com.jakuch.PartySheetShow.initiativeTracker.service.InitiativeTrackerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

@Controller
@AllArgsConstructor
public class InitiativeTrackerController {

    private InitiativeTrackerService initiativeTrackerService;

    @GetMapping("/initiativeTracker")
    public String get(InitiativeTrackerForm initiativeTracker, Model model) {
        model.addAttribute("initiativeTracker", initiativeTracker);
        return "initiativeTracker";
    }

    @RequestMapping(value = "/initiativeTracker", params = {"saveTracker"})
    public String saveTracker(@ModelAttribute("initiativeTracker") InitiativeTrackerForm initiativeTracker, Model model) {
        var initiativeId = initiativeTrackerService.saveInitiativeTracker(initiativeTracker);
        model.addAttribute("initiativeId", initiativeId);
        return "initiativeTracker";
    }

    @RequestMapping(value = "/initiativeTracker", params = {"loadTracker"})
    public String loadTracker(@RequestParam(value = "id") String id, Model model) {
        var initiativeTrackerForm = initiativeTrackerService.loadInitiativeTracker(id);
        model.addAttribute("initiativeTracker", initiativeTrackerForm);
        return "initiativeTracker";
    }

    @RequestMapping(value = "/initiativeTracker", params = {"addRow"})
    public String addRow(InitiativeTrackerForm initiativeTracker, Model model) {
        initiativeTracker.getInitiativeList().add(new InitiativeForm());
        model.addAttribute("initiativeTracker", initiativeTracker);
        return "initiativeTracker";
    }

    @RequestMapping(value = "/initiativeTracker", params = {"removeRow"})
    public String removeRow(InitiativeTrackerForm initiativeTracker, Model model) {
        if (!initiativeTracker.getInitiativeList().isEmpty()) {
            initiativeTracker.getInitiativeList().remove(initiativeTracker.getInitiativeList().size() - 1);
        }
        model.addAttribute("initiativeTracker", initiativeTracker);
        return "initiativeTracker";
    }

    @RequestMapping(value = "/initiativeTracker", params = {"sort"})
    public String sortInitiative(InitiativeTrackerForm initiativeTracker, Model model) {
        initiativeTracker.getInitiativeList().sort(Comparator.comparing(InitiativeForm::getValue, Comparator.nullsLast(Integer::compareTo)).reversed());
        model.addAttribute("initiativeTracker", initiativeTracker);
        return "initiativeTracker";
    }
}
