package fr.normanbet.paris.p2024.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 150)
    private String name;

    @ManyToOne(optional = false)
    private City city;

    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    @ManyToMany(mappedBy = "venues", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Sport> sports = new HashSet<>();
}

