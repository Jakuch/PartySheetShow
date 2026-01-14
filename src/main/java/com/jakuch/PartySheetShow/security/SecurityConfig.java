package com.jakuch.PartySheetShow.security;

import com.jakuch.PartySheetShow.security.repository.AppUsersRepository;
import com.jakuch.PartySheetShow.security.service.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, AppUserDetailsService appUserDetailsService) throws Exception {
        security
                .userDetailsService(appUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                "/",
                                        "/home",
                                        "/login",
                                        "/register",
                                        "/initiativeTracker",
                                        "/levelsTable",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/open5e/**"
                                )
                                .permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
