package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
}
