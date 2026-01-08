package com.jakuch.PartySheetShow.security.controller;

import com.jakuch.PartySheetShow.security.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserController {

    private AppUserService appUserService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", appUserService.findAll());
        return "admin/users";
    }

    @RequestMapping(value = "/admin/users", params = {"delete"})
    public String deleteById(@RequestParam String id) {
        appUserService.deleteById(id);
        return "redirect:/users";
    }
}
