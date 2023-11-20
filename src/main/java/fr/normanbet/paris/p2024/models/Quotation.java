package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "quotation")
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected float quotation = 1.0f;

    protected boolean isWinner = false;

    protected int score = 0;

    protected int rank = 0;

    protected int number = 0;

    protected int order = 0;

    protected String organization;

    @ManyToMany(cascade = CascadeType.MERGE)
    protected Set<Event> previousEvents = new LinkedHashSet<>();

    @ManyToOne
    protected Event event;

    public boolean isGold() {
        return event.isFinal() && this.rank == 1;
    }

    public boolean isSilver() {
        return event.isFinal() && this.rank == 2;
    }

    public boolean isBronze() {
        return event.isFinal() && this.rank == 3;
    }
}


