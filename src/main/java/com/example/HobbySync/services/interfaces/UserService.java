package com.example.HobbySync.services.interfaces;
import com.example.HobbySync.dtos.AuthenticationDTO;
import com.example.HobbySync.dtos.RegistrationDTO;
import com.example.HobbySync.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {
    UserDTO login(AuthenticationDTO authenticationDTO);
    UserDTO register(RegistrationDTO registrationDTO);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
