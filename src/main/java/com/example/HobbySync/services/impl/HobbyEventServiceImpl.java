package com.example.HobbySync.services.impl;

import com.example.HobbySync.dtos.HobbyEventDTO;
import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.model.User;
import com.example.HobbySync.repository.HobbyEventRepository;
import com.example.HobbySync.repository.HobbyGroupRepository;
import com.example.HobbySync.repository.UserRepository;
import com.example.HobbySync.services.interfaces.HobbyEventService;
import com.example.HobbySync.utils.exception.NotFoundException;
import com.example.HobbySync.utils.mapper.HobbyEventMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HobbyEventServiceImpl implements HobbyEventService {

    private final HobbyEventRepository hobbyEventRepository;
    private final HobbyGroupRepository hobbyGroupRepository;
    private final UserRepository userRepository;
    private final HobbyEventMapper hobbyEventMapper;

    @Value("${openai.api.key}")
    private String apiKey;

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

        List<User> participants = hobbyEventRepository.findParticipantsByEventId(eventId);
        hobbyEvent.setParticipants(new HashSet<>(participants));

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

    @Override
    public void addParticipant(UUID eventId, UUID userId) throws NotFoundException {
        HobbyEvent event = hobbyEventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        event.getParticipants().add(user);
        hobbyEventRepository.save(event);
    }
    public List<HobbyEventDTO> recommendEvents(String userDescription) throws Exception {
        List<HobbyEvent> allEvents = hobbyEventRepository.findAll();
        List<HobbyEventDTO> allEventDTOs = allEvents.stream()
                .map(hobbyEventMapper::toDTO)
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String eventsJson = mapper.writeValueAsString(allEventDTOs);

        String prompt = "Here are some events:\n" + eventsJson + "\n\nBased on the following user description, recommend the most relevant events:\n" + userDescription;

        String responseJson = callOpenAiApi(prompt);

        ObjectMapper responseMapper = new ObjectMapper();
        responseMapper.registerModule(new JavaTimeModule());
        responseMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Check for error in the response
        if (responseJson.contains("\"error\"")) {
            throw new Exception("Error from OpenAI API: " + responseJson);
        }

        List<HobbyEventDTO> recommendedEventDTOs = responseMapper.readValue(responseJson, new TypeReference<List<HobbyEventDTO>>() {});

        return recommendedEventDTOs;
    }

    private String callOpenAiApi(String prompt) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.openai.com/v1/completions");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + apiKey);

            String json = "{\"model\":\"text-davinci-003\",\"prompt\":\"" + prompt + "\",\"max_tokens\":1000}";
            httpPost.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity());
                return responseString;
            }
        }
    }
}