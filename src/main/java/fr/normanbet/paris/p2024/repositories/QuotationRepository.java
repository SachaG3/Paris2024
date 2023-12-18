package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Quotation;
import fr.normanbet.paris.p2024.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByEventId(Long eventId);
}
