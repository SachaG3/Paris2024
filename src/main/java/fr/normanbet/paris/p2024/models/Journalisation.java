package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Journalisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Sport sport;

    private String addedBy;

    private Date addedAt;

    public Journalisation() {
        this.addedAt = new Date();
    }

    public String getSportName() {
        return sport != null ? sport.getName() : null;
    }
}
