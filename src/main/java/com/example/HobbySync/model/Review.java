package com.example.HobbySync.model;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review extends BaseEntity {
    private String comment;
    private float rating;

    // Define one-to-one association with User
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Define many-to-one association with HobbyEvent
    @ManyToOne
    @JoinColumn(name = "hobby_event_id")
    private HobbyEvent hobbyEvent;
}

