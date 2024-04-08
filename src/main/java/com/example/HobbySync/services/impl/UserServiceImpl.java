package com.example.HobbySync.services.impl;
import com.example.HobbySync.dtos.AuthenticationDTO;
import com.example.HobbySync.dtos.RegistrationDTO;
import com.example.HobbySync.dtos.UserDTO;
import com.example.HobbySync.enums.TokenType;
import com.example.HobbySync.enums.UserType;
import com.example.HobbySync.model.Token;
import com.example.HobbySync.model.User;
import com.example.HobbySync.repository.TokenRepository;
import com.example.HobbySync.repository.UserRepository;
import com.example.HobbySync.security.JwtService;
import com.example.HobbySync.services.interfaces.UserService;
import com.example.HobbySync.utils.exception.AlreadyExistsException;
import com.example.HobbySync.utils.exception.BadInputException;
import com.example.HobbySync.utils.exception.NotFoundException;
import com.example.HobbySync.utils.mapper.UserMapper;
import com.example.HobbySync.utils.validation.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO login(AuthenticationDTO authenticationDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getEmail(), authenticationDTO.getPassword()
                )
        );
        User user = userRepository.findByEmail(authenticationDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("There is no user registered with this email!"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return userMapper.entityToDTO(user, jwtToken, refreshToken);
    }

    @Override
    public UserDTO register(RegistrationDTO registrationDTO) {

        if(userValidator.validateName(registrationDTO.getFirstName())) {
            throw new BadInputException("Invalid first name!");
        }
        if(userValidator.validateName(registrationDTO.getLastName())) {
            throw new BadInputException("Invalid last name!");
        }
        if(!userValidator.validateUsername(registrationDTO.getUsername())) {
            throw new BadInputException("Invalid username!");
        }
        if(!userValidator.validateEmail(registrationDTO.getEmail())) {
            throw new BadInputException("The provided email is not correct!");
        }
        if(!userValidator.validatePassword(registrationDTO.getPassword())) {
            throw new BadInputException("Your password doesn't meet the requirements!");
        }
        userRepository.findByEmail(registrationDTO.getEmail()).ifPresent(
                user -> {
                    throw new AlreadyExistsException("A user with this email already exists!");
                }
        );
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .email(registrationDTO.getEmail())
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .bio(registrationDTO.getBio())
                .profilePicture(registrationDTO.getProfilePicture())
                .userType(UserType.valueOf(registrationDTO.getUserType()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return userMapper.entityToDTO(user, jwtToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NotFoundException("There is no user registered with this email!"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = UserDTO.builder()
                        .jwtToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
