package com.example.HobbySync.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventReviewDTO {
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventDateTime;
    private String eventDescription;
    private String reviewComment;
    private float reviewRating;
}
