package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.QuotationTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationTeamRepository extends JpaRepository<QuotationTeam, Long> {

}
