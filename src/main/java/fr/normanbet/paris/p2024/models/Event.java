package fr.normanbet.paris.p2024.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import fr.normanbet.paris.p2024.models.types.Quotation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateEvent;

    private boolean terminated;

    @Column(length = 100)
    private String description;

    @Column(length = 150)
    private String location;

    @OneToMany(mappedBy = "event")
    private Set<Quotation> quotations=new HashSet<>();

    @ManyToOne
    private Discipline discipline;

    @ManyToOne
    private Olympiad olympiad;

    @ManyToOne
    private City city;

}
