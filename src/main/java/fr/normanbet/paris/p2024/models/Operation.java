package fr.normanbet.paris.p2024.models;

import fr.normanbet.paris.p2024.models.types.OperationType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        return dateFormat.format(this.dateO);
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
        return this.amount + " €";
    }
}

