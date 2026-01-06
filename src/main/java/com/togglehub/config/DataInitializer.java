package com.togglehub.config;

import com.togglehub.domain.Role;
import com.togglehub.domain.User;
import com.togglehub.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            try {
                userService.loadUserByUsername("admin");
            } catch (Exception e) {
                userService.createUser(User.builder()
                        .username("admin")
                        .password("admin")
                        .enabled(true)
                        .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_MANAGER))
                        .build());
            }
            
            try {
                 userService.loadUserByUsername("manager");
            } catch (Exception e) {
                 userService.createUser(User.builder()
                        .username("manager")
                        .password("manager")
                        .enabled(true)
                        .roles(Set.of(Role.ROLE_MANAGER))
                        .build());
            }
        };
    }
}
