package fr.normanbet.paris.p2024.repositories;


import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByDiscipline(Discipline discipline);
}
