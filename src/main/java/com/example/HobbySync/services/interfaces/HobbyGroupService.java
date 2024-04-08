package com.example.HobbySync.services.interfaces;

import com.example.HobbySync.dtos.HobbyGroupDTO;

import java.util.List;
import java.util.UUID;

public interface HobbyGroupService {
    HobbyGroupDTO createHobbyGroup(HobbyGroupDTO hobbyGroupDTO);

    HobbyGroupDTO getHobbyGroup(UUID id);

    List<HobbyGroupDTO> getAllHobbyGroups();

    HobbyGroupDTO updateHobbyGroup(UUID id, HobbyGroupDTO hobbyGroupDTO);

    void deleteHobbyGroup(UUID id);
}
