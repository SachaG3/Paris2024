package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateEventAfter(LocalDateTime.now());
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
}
