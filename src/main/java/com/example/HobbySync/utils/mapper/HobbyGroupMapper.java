package com.example.HobbySync.utils.mapper;

import com.example.HobbySync.dtos.HobbyGroupDTO;
import com.example.HobbySync.model.HobbyGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HobbyGroupMapper {
    public HobbyGroupDTO toDTO(HobbyGroup hobbyGroup) {
        return HobbyGroupDTO.builder()
                .id(hobbyGroup.getId())
                .name(hobbyGroup.getName())
                .description(hobbyGroup.getDescription())
                .image(hobbyGroup.getImage())
                .members(hobbyGroup.getMembers())
                .events(hobbyGroup.getEvents())
                .build();
    }
    public HobbyGroup toEntity(HobbyGroupDTO hobbyGroupDTO) {
        return HobbyGroup.builder()
                .name(hobbyGroupDTO.getName())
                .description(hobbyGroupDTO.getDescription())
                .image(hobbyGroupDTO.getImage())
                .members(hobbyGroupDTO.getMembers())
                .events(hobbyGroupDTO.getEvents())
                .build();
    }
    public List<HobbyGroupDTO> toDTOs(List<HobbyGroup> hobbyGroups) {
        return hobbyGroups.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
