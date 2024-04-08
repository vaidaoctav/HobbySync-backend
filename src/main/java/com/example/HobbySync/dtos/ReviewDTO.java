package com.example.HobbySync.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private UUID id;
    private String comment;
    private float rating;
    private UUID userId;
    private UUID eventId;
}

