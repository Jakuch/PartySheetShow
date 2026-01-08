package com.jakuch.PartySheetShow.security.service;

import com.jakuch.PartySheetShow.security.form.RegistrationForm;
import com.jakuch.PartySheetShow.security.model.AppUser;
import com.jakuch.PartySheetShow.security.model.AppUserDetails;
import com.jakuch.PartySheetShow.security.model.AppUserRole;
import com.jakuch.PartySheetShow.security.repository.AppUsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUsersRepository appUsersRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationForm registrationForm) {
        var user = AppUser.builder()
                .username(registrationForm.getUsername())
                .password(passwordEncoder.encode(registrationForm.getPassword()))
                .roles(sanitizeRoles(registrationForm.getRoles()))
                .build();

        appUsersRepository.save(user);
    }

    private List<AppUserRole> sanitizeRoles(List<AppUserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            roles = List.of(AppUserRole.ROLE_PLAYER);
        }

        return roles.stream()
                .filter(AppUserRole.getCorrectRoles()::contains)
                .distinct()
                .toList();
    }

    public List<AppUser> findAll() {
        return appUsersRepository.findAll();
    }

    public void deleteById(String id) {
        appUsersRepository.deleteById(id);
    }

    public boolean isUsernamePresent(String username) {
        return appUsersRepository.findByUsername(username).isPresent();
    }

    public AppUser getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ((AppUserDetails) auth.getPrincipal()).getAppUser();
    }

}
