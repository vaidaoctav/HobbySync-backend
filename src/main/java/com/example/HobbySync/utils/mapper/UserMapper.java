package com.example.HobbySync.utils.mapper;
import com.example.HobbySync.dtos.UserDTO;
import com.example.HobbySync.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO entityToDTO(User user, String jwtToken, String refreshToken) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .xCoord(user.getXCoord())
                .yCoord(user.getYCoord())
                .userType(user.getUserType())
                .jwtToken(jwtToken)
                .refreshToken(refreshToken).build();
    }
   public UserDTO entityToDTO(User user) {
       return UserDTO.builder()
               .id(user.getId())
               .username(user.getUsername())
               .email(user.getEmail())
               .firstName(user.getFirstName())
               .lastName(user.getLastName())
               .bio(user.getBio())
               .profilePicture(user.getProfilePicture())
               .xCoord(user.getXCoord())
               .yCoord(user.getYCoord())
               .userType(user.getUserType()).build();
   }
}
