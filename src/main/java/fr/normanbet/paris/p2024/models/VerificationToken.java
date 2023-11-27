package fr.normanbet.paris.p2024.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Entity
@Data
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;



    public VerificationToken() {
    }
    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
    // Getters et Setters
}
