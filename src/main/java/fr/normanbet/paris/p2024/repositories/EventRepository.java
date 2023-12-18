package fr.normanbet.paris.p2024.repositories;


import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByDiscipline(Discipline discipline);
    List<Event> findByDateEventAfter(LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE e.dateEvent >= CURRENT_DATE")
    List<Event> findCurrentAndFutureEvents();
}
