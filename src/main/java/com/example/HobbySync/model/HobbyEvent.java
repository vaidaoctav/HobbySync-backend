package com.example.HobbySync.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hobby_event")
public class HobbyEvent extends BaseEntity {
    private String name;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    private String description;
    private Integer capacity;
    private Integer fee;
    @Column(name = "x_coord")
    private Double xCoord;
    @Column(name = "y_coord")
    private Double yCoord;

    @ManyToOne
    @JoinColumn(name = "hobby_group_id")
    private HobbyGroup hobbyGroup;

    @ManyToMany
    @JoinTable(
            name = "user_hobby_event",
            joinColumns = @JoinColumn(name = "hobby_event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();
}

