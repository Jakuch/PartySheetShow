package com.jakuch.partySheetShow.security.form;

import com.jakuch.partySheetShow.security.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    private List<UserRole> roles = new ArrayList<>();
}
