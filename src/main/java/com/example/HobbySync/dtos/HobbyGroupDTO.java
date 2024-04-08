package com.example.HobbySync.dtos;

import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HobbyGroupDTO {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private Set<User> members;
    private Set<HobbyEvent> events;

}

