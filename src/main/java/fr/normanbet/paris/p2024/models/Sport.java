package fr.normanbet.paris.p2024.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.normanbet.paris.p2024.models.types.DisciplineType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"name"})
@Entity
public class Sport extends OlympicElement {

    @Column()
    private boolean individual;

    @Column(length = 150)
    private String name;

    @Column(length = 150)
    private String description;

    @ManyToOne(optional = true)
    @JsonIgnore
    private Sport parent;

    private boolean collective;

    private int sizeTeam;

    @OneToMany(mappedBy = "sport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Discipline> disciplines = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Venue> venues = new HashSet<>();


    public void setDisciplinesLines(String lines) {
        String[] disciplinesLines = lines.split("\r\n");
        for (String line : disciplinesLines) {
            Discipline discipline = new Discipline();
            String[] fields = line.split(";");
            discipline.setName(fields[0]);
            if (fields.length > 1) {
                String t = fields[1];
                discipline.setType(t.equals("H") ? DisciplineType.HOMMES : (t.equals("F") ? DisciplineType.FEMMES : DisciplineType.MIXTE));
            }
            discipline.setSport(this);
            this.disciplines.add(discipline);
        }
    }

}

