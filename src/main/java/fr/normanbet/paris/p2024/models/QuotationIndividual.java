package fr.normanbet.paris.p2024.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuotationIndividual extends Quotation {

    @ManyToOne
    private Athlete athlete;
}


