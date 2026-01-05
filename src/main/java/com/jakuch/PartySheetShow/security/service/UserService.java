package com.jakuch.PartySheetShow.security.service;

import com.jakuch.PartySheetShow.security.form.RegistrationForm;
import com.jakuch.PartySheetShow.security.model.AppUser;
import com.jakuch.PartySheetShow.security.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationForm registrationForm) {
        var user = new AppUser();
        user.setUsername(registrationForm.getUsername());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setRoles(registrationForm.getRoles());

        usersRepository.save(user);
    }

    public List<AppUser> findAll() {
        return usersRepository.findAll();
    }

    public String deleteById(@RequestParam String id) {
        usersRepository.deleteById(id);
        return "redirect:/users";
    }

    public boolean isUsernamePresent(String username) {
        return usersRepository.findByUsername(username).isPresent();
    }

}
