package com.example.HobbySync.services.impl;

import com.example.HobbySync.dtos.ReviewDTO;
import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.model.Review;
import com.example.HobbySync.model.User;
import com.example.HobbySync.repository.HobbyEventRepository;
import com.example.HobbySync.repository.ReviewRepository;
import com.example.HobbySync.repository.UserRepository;
import com.example.HobbySync.services.interfaces.ReviewService;
import com.example.HobbySync.utils.exception.BadInputException;
import com.example.HobbySync.utils.exception.NotFoundException;
import com.example.HobbySync.utils.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final HobbyEventRepository hobbyEventRepository;

    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) throws BadInputException {
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new BadInputException("User not found with id: " + reviewDTO.getUserId()));

        HobbyEvent hobbyEvent = hobbyEventRepository.findById(reviewDTO.getEventId())
                .orElseThrow(() -> new BadInputException("Hobby event not found with id: " + reviewDTO.getEventId()));

        Review review = Review.builder()
                .comment(reviewDTO.getComment())
                .rating(reviewDTO.getRating())
                .user(user)
                .hobbyEvent(hobbyEvent)
                .build();

        return reviewMapper.toDTO(reviewRepository.save(review));
    }


    @Override
    public ReviewDTO getReview(UUID reviewId) throws NotFoundException {
        return reviewMapper.toDTO(reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found!")));
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO) throws BadInputException, NotFoundException {
        Review existingReview = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewDTO.getId()));

        existingReview.setComment(reviewDTO.getComment());
        existingReview.setRating(reviewDTO.getRating());

        Review updatedReview = reviewRepository.save(existingReview);

        return reviewMapper.toDTO(updatedReview);
    }


    @Override
    public void deleteReview(UUID reviewId) throws NotFoundException {
        // Verifică dacă recenzia există în baza de date
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("Review not found with id: " + reviewId);
        }

        // Șterge recenzia din baza de date
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewDTO> getReviewsForEvent(UUID eventId) {
        // Găsește toate recenziile asociate evenimentului cu eventId
        List<Review> reviews = reviewRepository.findByHobbyEventId(eventId);

        // Convertște lista de recenzii în DTO-uri
        return reviews.stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageRatingForEvent(UUID eventId) {
        // Găsește toate recenziile asociate evenimentului cu eventId
        List<Review> reviews = reviewRepository.findByHobbyEventId(eventId);

        // Calculează media notelor recenziilor
        if (!reviews.isEmpty()) {
            double totalRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .sum();
            return totalRating / reviews.size();
        } else {
            return 0; // Dacă nu există recenzii, returnează 0
        }
    }

}
