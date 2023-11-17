package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.types.OperationType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal amount;
    private OperationType type;
    private LocalDateTime dateO;

    @ManyToOne
    private User user;

    public boolean isDeposit() {
        return OperationType.DEPOSIT.equals(this.type);
    }

    public boolean isWithdrawal() {
        return OperationType.WITHDRAWAL.equals(this.type);
    }

    public String getFormattedDate() {
        return dateO.format(DateTimeFormatter.ofPattern("EEEE d MMMM uuuu 'à' HH'h'mm'min'"));
    }
    public String getFormattedDate2() {
        return dateO.format(DateTimeFormatter.ofPattern("EEEE d MMMM uuuu"));
    }
    public String getRelativeFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate yesterday = today.minusDays(1);
        LocalDate dateOfOperation = dateO.toLocalDate();

        if (dateOfOperation.equals(today)) {
            // Si l'opération est d'aujourd'hui, affichez l'heure
            return dateO.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (dateOfOperation.equals(yesterday)) {
            // Si l'opération est d'hier
            return "Hier";
        } else if (dateO.isAfter(now.minusWeeks(1))) {
            // Si l'opération est dans la semaine passée
            return dateO.format(DateTimeFormatter.ofPattern("EEEE", Locale.FRANCE));
        } else {
            // Pour les dates plus anciennes
            return dateO.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.FRANCE));
        }
    }




    public String getFormattedTime() {
        return dateO.format(DateTimeFormatter.ofPattern("ss"));
    }

    public String getTranslatedType() {
        switch (this.type) {
            case DEPOSIT:
                return "Dépôt";
            case WITHDRAWAL:
                return "Retrait";
            default:
                return this.type.name(); // ou .toString() si type est une String
        }
    }

    public String getFormattedAmount() {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.FRANCE);
        return formatter.format(this.amount) + " €";
    }
}

