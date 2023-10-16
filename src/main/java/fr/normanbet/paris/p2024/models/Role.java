package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(length = 50)
    private String name;

}
