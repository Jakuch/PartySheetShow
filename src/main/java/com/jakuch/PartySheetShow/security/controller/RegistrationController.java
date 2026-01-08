package com.jakuch.PartySheetShow.security.controller;

import com.jakuch.PartySheetShow.security.form.RegistrationForm;
import com.jakuch.PartySheetShow.security.model.AppUserRole;
import com.jakuch.PartySheetShow.security.service.AppUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final AppUserService appUserService;

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        model.addAttribute("availableRoles", AppUserRole.getCorrectRoles());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegistrationForm form,
                           BindingResult bindingResult,
                           Model model) {

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }
        if (!bindingResult.hasFieldErrors("username") && appUserService.isUsernamePresent(form.getUsername())) {
            bindingResult.rejectValue("username", "username.taken", "Username is already taken");
        }
        if (form.getRoles() == null || form.getRoles().isEmpty()) {
            bindingResult.rejectValue("roles", "role.missing", "At least one role should be selected");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("availableRoles", AppUserRole.getCorrectRoles());
            return "register";
        }

        appUserService.register(form);
        return "redirect:/login?registered";
    }
}
