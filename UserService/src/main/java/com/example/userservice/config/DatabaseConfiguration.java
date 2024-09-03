package com.example.userservice.config;

import com.example.userservice.dtos.UserRequestDTO;
import com.example.userservice.entities.UserRole;
import com.example.userservice.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            if (userService.findUsers().isEmpty()) {
                UserRequestDTO defaultAdmin = UserRequestDTO.builder()
                        .username("adminul")
                        .password("admin")
                        .firstName("admin")
                        .lastName("admin")
                        .email("admin@mail.com")
                        .role(UserRole.ADMIN)
                        .build();
                UserRequestDTO defaultUser = UserRequestDTO.builder()
                        .username("Userul")
                        .password("user")
                        .firstName("user")
                        .lastName("user")
                        .email("user@mail.com")
                        .role(UserRole.USER)
                        .build();
                UserRequestDTO defaultUser2 = UserRequestDTO.builder()
                        .username("Userul2")
                        .password("user2")
                        .firstName("user2")
                        .lastName("user2")
                        .email("user2@mail.com")
                        .role(UserRole.USER)
                        .build();
                System.out.println("defaultUser:" + userService.register(defaultUser).getAccessToken());
                System.out.println("defaultUser2:" + userService.register(defaultUser2).getAccessToken());
                System.out.println("defaultAdmin:" + userService.register(defaultAdmin).getAccessToken());
            } else {
                return;
            }
        };
    }
}