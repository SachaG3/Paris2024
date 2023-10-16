package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Olympiad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int year;

    @Column(length = 10)
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    private City city;

    private boolean summer;

    @ManyToMany(mappedBy = "olympiads",cascade = CascadeType.ALL)
    private List<Discipline> disciplines=new ArrayList<>();
}
