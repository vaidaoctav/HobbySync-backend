package com.example.HobbySync.repository;

import com.example.HobbySync.model.HobbyEvent;
import com.example.HobbySync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface HobbyEventRepository extends JpaRepository<HobbyEvent, UUID>{
    @Query("SELECT u FROM User u JOIN u.participatedEvents he WHERE he.id = :eventId")
    List<User> findParticipantsByEventId(@Param("eventId") UUID eventId);
}
