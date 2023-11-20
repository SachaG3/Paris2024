package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.models.*;
import fr.normanbet.paris.p2024.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private EventRepository eventRepository;

    public Bet placeBet(User user, Long eventId, float amount, float quotation) {
        // Logique pour placer un pari
        Event event = eventRepository.findById(eventId).orElseThrow();
        Bet bet = new Bet();
        // Configurer et enregistrer le pari
        return betRepository.save(bet);
    }

    public Event getEventDetails(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }
}
