package com.example.HobbySync.repository;

import com.example.HobbySync.model.HobbyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface HobbyEventRepository extends JpaRepository<HobbyEvent, UUID>{
}
