package com.example.HobbySync.services.interfaces;

import com.example.HobbySync.dtos.EventReviewDTO;
import com.example.HobbySync.dtos.ReviewDTO;
import com.example.HobbySync.utils.exception.BadInputException;
import com.example.HobbySync.utils.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewDTO addReview(ReviewDTO reviewDTO) throws BadInputException;
    ReviewDTO getReview(UUID reviewId) throws NotFoundException;
    ReviewDTO updateReview(ReviewDTO reviewDTO) throws BadInputException, NotFoundException;
    void deleteReview(UUID reviewId) throws NotFoundException;
    List<ReviewDTO> getReviewsForEvent(UUID eventId);
    double getAverageRatingForEvent(UUID eventId);
    List<EventReviewDTO> getEventReviewsForUser(UUID userId);
}
