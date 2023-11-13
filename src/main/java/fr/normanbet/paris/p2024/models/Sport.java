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

    @Column(length = 150)
    private String log;

    public void setDisciplinesLines(String lines){
        String[] disciplinesLines=lines.split("\r\n");
        for (String line:disciplinesLines){
            Discipline discipline=new Discipline();
            String[] fields=line.split(";");
            discipline.setName(fields[0]);
            if(fields.length>1){
                String t=fields[1];
                discipline.setType(t.equals("H")? DisciplineType.HOMMES:(t.equals("F")?DisciplineType.FEMMES:DisciplineType.MIXTE));
            }
            discipline.setSport(this);
            this.disciplines.add(discipline);
        }
    }

}
