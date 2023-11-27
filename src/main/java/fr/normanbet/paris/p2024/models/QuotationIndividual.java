package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quotation_individual")
@PrimaryKeyJoinColumn(name = "id")
public class QuotationIndividual extends Quotation {

    @ManyToOne(optional = false)
    private Participation participation;

    @PreRemove
    private void removeQuotationFromParticipation() {
        this.participation.getQuotationsIndividual().remove(this);
    }

}




