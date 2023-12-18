package fr.normanbet.paris.p2024.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.normanbet.paris.p2024.models.types.DisciplineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String name;

    private DisciplineType type=DisciplineType.HOMMES;

    @OneToMany(mappedBy = "discipline")
    private List<Event> events=new ArrayList<>();

    @ManyToOne(optional = false)
    @JsonIgnore
    private Sport sport;

    @ManyToMany
    private List<Olympiad> olympiads=new ArrayList<>();

}

