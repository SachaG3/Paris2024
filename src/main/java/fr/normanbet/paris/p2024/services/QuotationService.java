package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.models.Quotation;
import fr.normanbet.paris.p2024.repositories.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;


    public List<Quotation> getQuotationsForEvent(Event event) {
        return quotationRepository.findByEventId(event.getId());
    }
    public Quotation findById(Long id) {
        return quotationRepository.findById(id)
                .orElse(null);
    }
}
