package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.models.Quotation;
import fr.normanbet.paris.p2024.models.QuotationTeam;
import fr.normanbet.paris.p2024.repositories.EventRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event getEventWithTeams(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId) ;
        if (eventOptional.isPresent())  {
            Event event = eventOptional.get();
            Set<Quotation> quotations = event.getQuotations();
            Set<QuotationTeam> quotationTeams = quotations.stream()
                    .filter(QuotationTeam.class::isInstance)
                    .map(QuotationTeam.class::cast)
                    .collect(Collectors.toSet());

            return event;
        }
        return null;
    }
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateEventAfter(LocalDateTime.now());
    }


}
