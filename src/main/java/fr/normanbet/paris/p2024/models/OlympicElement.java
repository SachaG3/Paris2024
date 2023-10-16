package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OlympicElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    public abstract String getName();

    @ManyToMany
    protected List<News> news=new ArrayList<>();
}
