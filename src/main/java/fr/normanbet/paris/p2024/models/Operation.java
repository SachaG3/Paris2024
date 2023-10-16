package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.types.OperationType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private float amount;
    private OperationType type;
    private LocalDateTime dateO;

    @ManyToOne
    private User user;
}

