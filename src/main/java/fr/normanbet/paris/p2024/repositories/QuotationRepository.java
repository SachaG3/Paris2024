package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Quotation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository {
    List<Quotation> findAll();
}
