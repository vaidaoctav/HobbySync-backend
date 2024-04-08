package com.example.HobbySync.utils.mapper;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.model.HobbyEvent;
import org.springframework.stereotype.Component;

@Component
public class HobbyEventMapper {
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
                .participants(hobbyEvent.getParticipants())
                .hobbyGroupId(hobbyEvent.getHobbyGroup().getId())
                .build();
    }
    public HobbyEvent toEntity(HobbyEventDTO hobbyEventDTO) {
        return HobbyEvent.builder()
                .name(hobbyEventDTO.getName())
                .dateTime(hobbyEventDTO.getDateTime())
                .description(hobbyEventDTO.getDescription())
                .capacity(hobbyEventDTO.getCapacity())
                .fee(hobbyEventDTO.getFee())
                .xCoord(hobbyEventDTO.getXCoord())
                .yCoord(hobbyEventDTO.getYCoord())
                .participants(hobbyEventDTO.getParticipants())
                .build();
    }
}
