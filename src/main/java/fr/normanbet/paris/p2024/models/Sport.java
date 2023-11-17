package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.types.DisciplineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Sport extends OlympicElement{

    @Column(length = 150)
    private String name;

    @Column(length = 150)
    private String description;

    @ManyToOne(optional = true)
    private Sport parent;

    private boolean individual;

    @OneToMany(mappedBy = "sport",cascade = CascadeType.ALL)
    private List<Discipline> disciplines=new ArrayList<>();

    @Column(length = 150)
    private String addedBy;


    public void setDisciplinesLines(String lines) {
        String[] disciplinesNames = lines.split(",");
        for (String disciplineName : disciplinesNames) {
            Discipline discipline = new Discipline();
            discipline.setName(disciplineName.trim()); // trim pour supprimer les espaces autour du nom
            discipline.setType(DisciplineType.MIXTE);
            discipline.setSport(this);
            this.disciplines.add(discipline);
        }
    }

}
