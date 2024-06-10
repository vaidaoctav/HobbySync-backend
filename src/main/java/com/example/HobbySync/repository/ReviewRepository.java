package com.example.HobbySync.repository;

import com.example.HobbySync.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByHobbyEventId(UUID hobbyEventId);
    List<Review> findByUserId(UUID userId);
}
