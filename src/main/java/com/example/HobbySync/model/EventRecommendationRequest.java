package com.example.HobbySync.model;

import com.example.HobbySync.dtos.SimpleEventDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRecommendationRequest {
    private String prompt;
    private List<SimpleEventDTO> events;
}

