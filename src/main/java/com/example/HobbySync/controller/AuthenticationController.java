package com.example.HobbySync.controller;
import com.example.HobbySync.dtos.AuthenticationDTO;
import com.example.HobbySync.dtos.RegistrationDTO;
import com.example.HobbySync.dtos.UserDTO;
import com.example.HobbySync.repository.TokenRepository;
import com.example.HobbySync.services.interfaces.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/hobby-sync/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @PostMapping(value="/register",consumes = "multipart/form-data")
    public ResponseEntity<UserDTO> register(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("email") String email,
                                            @RequestParam("firstName") String firstName,
                                            @RequestParam("lastName") String lastName,
                                            @RequestParam("bio") String bio,
                                            @RequestParam(value="profilePicture",required = false) MultipartFile profilePicture,
                                            @RequestParam("userType") String userType) {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .bio(bio)
                .profilePicture(profilePicture)
                .userType(userType)
                .build();
        return ResponseEntity.ok(userService.register(registrationDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthenticationDTO authenticationDTO,HttpServletResponse response){
        UserDTO user=userService.login(authenticationDTO);
        ResponseCookie cookie=ResponseCookie.from("accessToken",user.getJwtToken())
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(60*60*24)
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie=ResponseCookie.from("refreshToken",user.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(60*60*24*7)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie",cookie.toString());
        response.addHeader("Set-Cookie",refreshCookie.toString());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        userService.refreshToken(httpServletRequest, httpServletResponse);
    }
    @GetMapping("/validate-session")
    public ResponseEntity<?> validateSession(HttpServletRequest request) {
        String jwt = extractJwtFromCookie(request, "accessToken");
        if (jwt != null && !jwt.isEmpty()) {
            return tokenRepository.findByToken(jwt)
                    .map(token -> {
                        if (!token.isExpired() && !token.isRevoked()) {
                            return ResponseEntity.ok().build(); // Token is valid
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Token is expired or revoked
                    })
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // Token not found
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}

