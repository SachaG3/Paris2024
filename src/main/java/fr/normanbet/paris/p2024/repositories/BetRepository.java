package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Bet;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.types.EventStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByUser(User user);
    @Query("SELECT b FROM Bet b WHERE b.officialQuotation.event.status = :status")
    List<Bet> findAllBetsByEventStatus(@Param("status") EventStatusType status);
    @Query("SELECT b FROM Bet b JOIN FETCH b.officialQuotation o JOIN FETCH o.event e JOIN FETCH e.venue WHERE e.status = :status")
    List<Bet> findAllBetsWithDetailsByEventStatus(@Param("status") EventStatusType status);



}
