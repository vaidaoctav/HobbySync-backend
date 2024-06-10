package com.example.HobbySync.dtos;

import com.example.HobbySync.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePicture;
    private Double xCoord;
    private Double yCoord;
    private UserType userType;
    @JsonProperty("access_token")
    private String jwtToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
