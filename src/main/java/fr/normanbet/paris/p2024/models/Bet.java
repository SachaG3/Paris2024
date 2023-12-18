package fr.normanbet.paris.p2024.models;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter

public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private BigDecimal amount;
    private float quotation;
    private LocalDateTime dateB;


    @ManyToOne
    private Quotation officialQuotation;

    @ManyToOne
    private User user;


}
