package fr.normanbet.paris.p2024.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
    private List<OlympicElement> links = new ArrayList<>();


    public String getFormattedDate() {
        return dateN.format(DateTimeFormatter.ofPattern("EEEE d MMMM uuuu 'à' HH'h'mm'min'"));
    }

    public String getFormattedDate2() {
        return dateN.format(DateTimeFormatter.ofPattern("EEEE d MMMM uuuu"));
    }

    public String getRelativeFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate yesterday = today.minusDays(1);
        LocalDate dateOfOperation = dateN.toLocalDate();

        if (dateOfOperation.equals(today)) {
            // Si l'opération est d'aujourd'hui, affichez l'heure
            return dateN.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (dateOfOperation.equals(yesterday)) {
            // Si l'opération est d'hier
            return "Hier";
        } else if (dateN.isAfter(now.minusWeeks(1))) {
            // Si l'opération est dans la semaine passée
            return dateN.format(DateTimeFormatter.ofPattern("EEEE", Locale.FRANCE));
        } else {
            // Pour les dates plus anciennes
            return dateN.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.FRANCE));
        }

    }

}



