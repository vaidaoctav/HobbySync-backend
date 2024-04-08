package com.example.HobbySync.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hobby_group")
public class HobbyGroup extends BaseEntity {
    private String name;
    private String description;
    private String image;

    @ManyToMany(mappedBy = "hobbyGroups")
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "hobbyGroup", cascade = CascadeType.ALL)
    private Set<HobbyEvent> events = new HashSet<>();
}
