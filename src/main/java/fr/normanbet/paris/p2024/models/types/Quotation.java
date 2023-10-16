package fr.normanbet.paris.p2024.models.types;

import fr.normanbet.paris.p2024.models.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public abstract class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected float quotation = 1.0f;

    @ManyToOne
    protected Event event;
}

