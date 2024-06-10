package com.example.HobbySync.utils.mapper;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.model.HobbyEvent;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HobbyEventMapper {
    private final UserMapper userMapper = new UserMapper();
    public HobbyEventDTO toDTO(HobbyEvent hobbyEvent) {
        return HobbyEventDTO.builder()
                .id(hobbyEvent.getId())
                .name(hobbyEvent.getName())
                .dateTime(hobbyEvent.getDateTime())
                .description(hobbyEvent.getDescription())
                .capacity(hobbyEvent.getCapacity())
                .fee(hobbyEvent.getFee())
                .xCoord(hobbyEvent.getXCoord())
                .yCoord(hobbyEvent.getYCoord())
                .participants(hobbyEvent.getParticipants().stream().map(userMapper::entityToDTO).collect(Collectors.toSet()))
                .hobbyGroupId(hobbyEvent.getHobbyGroup().getId())
                .build();
    }

}
