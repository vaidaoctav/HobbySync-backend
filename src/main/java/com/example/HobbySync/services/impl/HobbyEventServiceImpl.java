package com.example.HobbySync.services.impl;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.model.HobbyGroup;
import com.example.HobbySync.repository.HobbyEventRepository;
import com.example.HobbySync.repository.HobbyGroupRepository;
import com.example.HobbySync.services.interfaces.HobbyEventService;
import com.example.HobbySync.utils.exception.NotFoundException;
import com.example.HobbySync.utils.mapper.HobbyEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HobbyEventServiceImpl implements HobbyEventService {

    private final HobbyEventRepository hobbyEventRepository;
    private final HobbyGroupRepository hobbyGroupRepository;
    private final HobbyEventMapper hobbyEventMapper;

    @Override
    public HobbyEventDTO addHobbyEvent(HobbyEventDTO hobbyEventDTO) {
        var hobbyGroup = hobbyGroupRepository.findById(hobbyEventDTO.getHobbyGroupId()).orElseThrow(() -> new NotFoundException("Hobby group not found with ID: " + hobbyEventDTO.getHobbyGroupId()));
        var hobbyEvent = HobbyEvent.builder()
                .name(hobbyEventDTO.getName())
                .dateTime(hobbyEventDTO.getDateTime())
                .description(hobbyEventDTO.getDescription())
                .capacity(hobbyEventDTO.getCapacity())
                .fee(hobbyEventDTO.getFee())
                .xCoord(hobbyEventDTO.getXCoord())
                .yCoord(hobbyEventDTO.getYCoord())
                .hobbyGroup(hobbyGroup)
                .build();
        HobbyEvent savedEvent = hobbyEventRepository.save(hobbyEvent);
        return hobbyEventMapper.toDTO(savedEvent);
    }

    @Override
    public HobbyEventDTO getHobbyEvent(UUID eventId) throws NotFoundException {
        HobbyEvent hobbyEvent = hobbyEventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Hobby event not found with ID: " + eventId));
        return hobbyEventMapper.toDTO(hobbyEvent);
    }

    @Override
    public HobbyEventDTO updateHobbyEvent(HobbyEventDTO hobbyEventDTO) throws NotFoundException {
        HobbyEvent existingEvent = hobbyEventRepository.findById(hobbyEventDTO.getId())
                .orElseThrow(() -> new NotFoundException("Hobby event not found with ID: " + hobbyEventDTO.getId()));

        // Update the existing event with new details
        existingEvent.setName(hobbyEventDTO.getName());
        existingEvent.setDateTime(hobbyEventDTO.getDateTime());
        existingEvent.setDescription(hobbyEventDTO.getDescription());
        existingEvent.setCapacity(hobbyEventDTO.getCapacity());
        existingEvent.setFee(hobbyEventDTO.getFee());
        existingEvent.setXCoord(hobbyEventDTO.getXCoord());
        existingEvent.setYCoord(hobbyEventDTO.getYCoord());

        // Save the updated event
        HobbyEvent updatedEvent = hobbyEventRepository.save(existingEvent);
        return hobbyEventMapper.toDTO(updatedEvent);
    }

    @Override
    public void deleteHobbyEvent(UUID eventId) throws NotFoundException {
        if (!hobbyEventRepository.existsById(eventId)) {
            throw new NotFoundException("Hobby event not found with ID: " + eventId);
        }
        hobbyEventRepository.deleteById(eventId);
    }

    @Override
    public List<HobbyEventDTO> getAllHobbyEvents() {
        List<HobbyEvent> hobbyEvents = hobbyEventRepository.findAll();
        return hobbyEvents.stream()
                .map(hobbyEventMapper::toDTO)
                .collect(Collectors.toList());
    }
}