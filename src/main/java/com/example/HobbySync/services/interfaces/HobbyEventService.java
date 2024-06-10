package com.example.HobbySync.services.interfaces;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.utils.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface HobbyEventService {
    HobbyEventDTO addHobbyEvent(HobbyEventDTO hobbyEventDTO);

    HobbyEventDTO getHobbyEvent(UUID eventId) throws NotFoundException;

    HobbyEventDTO updateHobbyEvent(HobbyEventDTO hobbyEventDTO) throws NotFoundException;

    void deleteHobbyEvent(UUID eventId) throws NotFoundException;

    List<HobbyEventDTO> getAllHobbyEvents();

    void addParticipant(UUID eventId, UUID userId) throws NotFoundException;

    List<HobbyEventDTO> recommendEvents(String userDescription) throws Exception;
}
