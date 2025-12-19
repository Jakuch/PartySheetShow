package com.jakuch.PartySheetShow.security.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class AppUser {

    @Id
    private String id;

    private String username;

    private String password;

    private List<UserRole> roles;
}
