package com.example.HobbySync.controller;

import com.example.HobbySync.dtos.RegistrationDTO;
import com.example.HobbySync.dtos.UserDTO;
import com.example.HobbySync.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/hobby-sync")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;
    @PutMapping(value="/users/{id}",consumes = "multipart/form-data")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id,
                                            @RequestParam("username") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam("firstName") String firstName,
                                            @RequestParam("lastName") String lastName,
                                            @RequestParam("bio") String bio,
                                            @RequestParam(value="profilePicture",required = false) MultipartFile profilePicture) {
        var userDTO = RegistrationDTO.builder().firstName(firstName).lastName(lastName).email(email).bio(bio).profilePicture(profilePicture).username(username).build();

        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
