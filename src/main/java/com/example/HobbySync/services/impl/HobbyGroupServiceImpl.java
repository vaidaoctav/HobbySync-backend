package com.example.HobbySync.services.impl;

import com.example.HobbySync.dtos.HobbyGroupDTO;
import com.example.HobbySync.model.HobbyGroup;
import com.example.HobbySync.repository.HobbyGroupRepository;
import com.example.HobbySync.services.interfaces.HobbyGroupService;
import com.example.HobbySync.utils.exception.NotFoundException;
import com.example.HobbySync.utils.mapper.HobbyGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HobbyGroupServiceImpl implements HobbyGroupService {
    private final HobbyGroupRepository hobbyGroupRepository;
    private final HobbyGroupMapper hobbyGroupMapper;

    @Override
    public HobbyGroupDTO createHobbyGroup(HobbyGroupDTO hobbyGroupDTO) {
        HobbyGroup hobbyGroup = hobbyGroupMapper.toEntity(hobbyGroupDTO);
        HobbyGroup savedHobbyGroup = hobbyGroupRepository.save(hobbyGroup);
        return hobbyGroupMapper.toDTO(savedHobbyGroup);
    }

    @Override
    public HobbyGroupDTO getHobbyGroup(UUID id) {
        HobbyGroup hobbyGroup = hobbyGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HobbyGroup not found with id: " + id));
        return hobbyGroupMapper.toDTO(hobbyGroup);
    }

    @Override
    public List<HobbyGroupDTO> getAllHobbyGroups() {
        return hobbyGroupMapper.toDTOs(hobbyGroupRepository.findAll());
    }

    @Override
    public HobbyGroupDTO updateHobbyGroup(UUID id, HobbyGroupDTO hobbyGroupDTO) {
        HobbyGroup existingHobbyGroup = hobbyGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HobbyGroup not found with id: " + id));

        HobbyGroup updatedHobbyGroup = hobbyGroupRepository.save(existingHobbyGroup);
        return hobbyGroupMapper.toDTO(updatedHobbyGroup);
    }

    @Override
    public void deleteHobbyGroup(UUID id) {
        HobbyGroup hobbyGroup = hobbyGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HobbyGroup not found with id: " + id));
        hobbyGroupRepository.delete(hobbyGroup);
    }
}

