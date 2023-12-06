package fr.normanbet.paris.p2024.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
@Getter
@Setter
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 255)
    private String title;

    @Column(length = Integer.MAX_VALUE)
    private String content;

    private LocalDateTime dateN;

    private String mainImg;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(mappedBy = "news")
    private List<OlympicElement> links=new ArrayList<>();


}
