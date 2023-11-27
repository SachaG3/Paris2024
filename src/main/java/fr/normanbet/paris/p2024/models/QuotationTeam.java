package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "quotation_team")
@PrimaryKeyJoinColumn(name = "id")
public class QuotationTeam extends Quotation {
    @ManyToOne
    private Country country;

    @ManyToMany(cascade = CascadeType.MERGE)
    @OrderBy("order ASC")
    private Set<Participation> teamMembers = new LinkedHashSet<>();
}


