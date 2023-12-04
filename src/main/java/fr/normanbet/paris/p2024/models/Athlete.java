package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.types.GenreType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Athlete extends OlympicElement{

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    @OneToMany(mappedBy = "athlete")
    private List<Participation> participations=new ArrayList<>();

    private LocalDate birthdate;

    private GenreType genre;

    @Override
    public String getName() {
        return firstname+" "+lastname;
    }

    public void setAddedBy(String currentUser) {
    }
}
