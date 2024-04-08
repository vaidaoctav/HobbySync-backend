package com.example.HobbySync.controller;

import com.example.HobbySync.dtos.ReviewDTO;
import com.example.HobbySync.services.interfaces.ReviewService;
import com.example.HobbySync.utils.exception.BadInputException;
import com.example.HobbySync.utils.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hobby-sync/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDTO getReview(@PathVariable UUID reviewId) {
        try {
            return reviewService.getReview(reviewId);
        } catch (NotFoundException e) {
            // You can handle the exception here if needed
            return null;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO addReview(@RequestBody ReviewDTO reviewDTO) throws BadInputException {
        return reviewService.addReview(reviewDTO);
    }

    @PutMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDTO updateReview(@PathVariable UUID reviewId, @RequestBody ReviewDTO reviewDTO)
            throws BadInputException, NotFoundException {
        reviewDTO.setId(reviewId);
        return reviewService.updateReview(reviewDTO);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable UUID reviewId) throws NotFoundException {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDTO> getReviewsForEvent(@PathVariable UUID eventId) {
        return reviewService.getReviewsForEvent(eventId);
    }

    @GetMapping("/event/{eventId}/average-rating")
    @ResponseStatus(HttpStatus.OK)
    public double getAverageRatingForEvent(@PathVariable UUID eventId) {
        return reviewService.getAverageRatingForEvent(eventId);
    }
}

