package com.example.HobbySync.dtos;

import com.example.HobbySync.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HobbyEventDTO {
    private UUID id;
    private String name;
    private LocalDateTime dateTime;
    private String description;
    private Integer capacity;
    private Integer fee;
    private Double xCoord;
    private Double yCoord;
    private Set<User> participants;
    private UUID hobbyGroupId;
}

