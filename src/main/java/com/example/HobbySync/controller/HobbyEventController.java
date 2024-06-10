package com.example.HobbySync.controller;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.services.interfaces.HobbyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hobby-sync/hobby-events")
public class HobbyEventController {
    private final HobbyEventService hobbyEventService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HobbyEventDTO getHobbyEvent(@PathVariable UUID id) {
        return hobbyEventService.getHobbyEvent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HobbyEventDTO createHobbyEvent(@RequestBody HobbyEventDTO hobbyEventDTO) {
        //generate UUID
        hobbyEventDTO.setId(UUID.randomUUID());
        return hobbyEventService.addHobbyEvent(hobbyEventDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HobbyEventDTO updateHobbyEvent(@PathVariable UUID id, @RequestBody HobbyEventDTO hobbyEventDTO) {
        hobbyEventDTO.setId(id);
        return hobbyEventService.updateHobbyEvent(hobbyEventDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHobbyEvent(@PathVariable UUID id) {
        hobbyEventService.deleteHobbyEvent(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HobbyEventDTO> getAllHobbyEvents() {
        return hobbyEventService.getAllHobbyEvents();
    }

    @PutMapping("/{eventId}/join")
    @ResponseStatus(HttpStatus.OK)
    public void joinHobbyEvent(@PathVariable UUID eventId, @RequestBody UUID userId) {
        hobbyEventService.addParticipant(eventId, userId);
    }

    @PostMapping("/recommend")
    @ResponseStatus(HttpStatus.OK)
    public List<HobbyEventDTO> recommendEvents(@RequestBody String userDescription) throws Exception {
        try {
            return hobbyEventService.recommendEvents(userDescription);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating recommendations", e);
        }
    }
}
