package fr.normanbet.paris.p2024.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Country extends OlympicElement {

    @Column(length = 3,unique = true)
    private String code;

    @Column(length = 150)
    private String name;

    @Column(length = 150)
    private String notes;


}

