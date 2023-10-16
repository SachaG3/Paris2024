package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = { @Index(columnList = "athlete_id,discipline_id,country_id,olympiad_id", unique = true) })
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private Discipline discipline;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Olympiad olympiad;
}

