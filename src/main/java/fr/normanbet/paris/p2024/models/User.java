package fr.normanbet.paris.p2024.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50,unique = true)
    private String login;

    @Column(length = 255)
    private String password;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    @Column(length = 255)
    private String email;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Operation> operations=new ArrayList<>();

    @ManyToMany
    private List<OlympicElement> favorites=new ArrayList<>();

}

