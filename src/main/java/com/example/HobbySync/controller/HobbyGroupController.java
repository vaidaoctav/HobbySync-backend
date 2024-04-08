package com.example.HobbySync.controller;

import com.example.HobbySync.dtos.HobbyGroupDTO;
import com.example.HobbySync.services.interfaces.HobbyGroupService;
import com.example.HobbySync.utils.exception.BadInputException;
import com.example.HobbySync.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hobby-sync/hobby-groups")
public class HobbyGroupController {

    private final HobbyGroupService hobbyGroupService;

    @PostMapping
    public HobbyGroupDTO addHobbyGroup(@RequestBody HobbyGroupDTO hobbyGroupDTO) throws BadInputException {
        return hobbyGroupService.createHobbyGroup(hobbyGroupDTO);
    }

    @GetMapping("/{groupId}")
    public HobbyGroupDTO getHobbyGroup(@PathVariable UUID groupId) throws NotFoundException {
        return hobbyGroupService.getHobbyGroup(groupId);
    }

    @GetMapping
    public List<HobbyGroupDTO> getAllHobbyGroups() {
        return hobbyGroupService.getAllHobbyGroups();
    }

    @PutMapping("/{groupId}")
    public HobbyGroupDTO updateHobbyGroup(@PathVariable UUID groupId, @RequestBody HobbyGroupDTO hobbyGroupDTO) throws NotFoundException, BadInputException {
        hobbyGroupDTO.setId(groupId);
        return hobbyGroupService.updateHobbyGroup(groupId,hobbyGroupDTO);
    }

    @DeleteMapping("/{groupId}")
    public void deleteHobbyGroup(@PathVariable UUID groupId) throws NotFoundException {
        hobbyGroupService.deleteHobbyGroup(groupId);
    }
}
