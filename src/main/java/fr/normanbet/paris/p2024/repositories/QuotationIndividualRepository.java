package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.QuotationIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationIndividualRepository extends JpaRepository<QuotationIndividual, Long> {

}
