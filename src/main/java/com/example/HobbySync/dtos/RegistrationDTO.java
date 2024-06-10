package com.example.HobbySync.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private MultipartFile profilePicture;
    private String userType;
}
