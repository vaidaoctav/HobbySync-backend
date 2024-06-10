package com.example.HobbySync.controller;
import com.example.HobbySync.dtos.SimpleEventDTO;
import com.example.HobbySync.model.ChatRequest;
import com.example.HobbySync.model.ChatResponse;
import com.example.HobbySync.model.EventRecommendationRequest;
import com.example.HobbySync.model.Message;
import com.example.HobbySync.repository.HobbyEventRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    private final HobbyEventRepository hobbyEventRepository;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/recommend-events")
    public List<UUID> recommendEvents(@RequestParam String prompt) {
        List<SimpleEventDTO> events = hobbyEventRepository.findAll().stream()
                .map(event -> SimpleEventDTO.builder()
                        .id(event.getId())
                        .name(event.getName())
                        .description(event.getDescription())
                        .build())
                .collect(Collectors.toList());

        String userPrompt = buildPrompt(prompt, events);
        List<Message> messages = List.of(new Message("user", userPrompt));

        ChatRequest request = ChatRequest.builder()
                .model(model)
                .messages(messages)
                .n(1)
                .temperature(0.7)  // sau orice altă valoare care consideri că este potrivită
                .build();

        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return Collections.emptyList();
        }

        String responseContent = response.getChoices().get(0).getMessage().getContent();
        List<UUID> recommendedEventIds = parseRecommendedEventIds(responseContent);

        return recommendedEventIds;
    }

    private String buildPrompt(String userDescription, List<SimpleEventDTO> events) {
        StringBuilder prompt = new StringBuilder(userDescription);
        prompt.append("\n\nEvenimente disponibile:\n");
        for (SimpleEventDTO event : events) {
            prompt.append("ID: ").append(event.getId()).append("\n");
            prompt.append("Nume: ").append(event.getName()).append("\n");
            prompt.append("Descriere: ").append(event.getDescription()).append("\n\n");
        }
        prompt.append("Recomandă evenimentele care se potrivesc cel mai bine descrierii utilizatorului. ");
        prompt.append("Returnează doar ID-urile evenimentelor, într-un format JSON cu o listă de ID-uri: [\"id1\", \"id2\", ...].");
        return prompt.toString();
    }

    private List<UUID> parseRecommendedEventIds(String responseContent) {
        try {
            return new ObjectMapper().readValue(responseContent, new TypeReference<List<UUID>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
