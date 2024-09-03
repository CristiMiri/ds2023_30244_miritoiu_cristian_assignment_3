package com.example.userservice.dtos;

import com.example.userservice.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
