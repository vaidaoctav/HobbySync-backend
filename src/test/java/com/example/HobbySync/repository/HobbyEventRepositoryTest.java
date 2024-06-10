package com.example.HobbySync.repository;

import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.repository.HobbyEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HobbyEventRepositoryTest {

    @Autowired
    private HobbyEventRepository hobbyEventRepository;

    @Test
    public void testFindByIdWithParticipants() {
        UUID eventId = UUID.fromString("ec77ef38-524f-48d1-8ee5-e5d4b8f573d3");
        Optional<HobbyEvent> event = hobbyEventRepository.findByIdWithParticipants(eventId);
        assertTrue(event.isPresent());
        assertFalse(event.get().getParticipants().isEmpty());
    }
}
