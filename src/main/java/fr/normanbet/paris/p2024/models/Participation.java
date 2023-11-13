package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "athlete_id,discipline_id,country_id,olympiad_id", unique = true)})
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne(fetch = FetchType.EAGER)
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    private Olympiad olympiad;

    private int number = 0;

    private int order = 0;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<QuotationIndividual> quotationsIndividual = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.LAZY)
    private Set<QuotationTeam> quotationTeams = new LinkedHashSet<>();
}


