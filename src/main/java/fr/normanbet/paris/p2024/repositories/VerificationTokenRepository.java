package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByUserId(Long userId);
}
