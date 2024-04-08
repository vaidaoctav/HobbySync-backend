package com.example.HobbySync.utils.mapper;

import com.example.HobbySync.dtos.ReviewDTO;
import com.example.HobbySync.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUser().getId())
                .eventId(review.getHobbyEvent().getId())
                .build();
    }
}
