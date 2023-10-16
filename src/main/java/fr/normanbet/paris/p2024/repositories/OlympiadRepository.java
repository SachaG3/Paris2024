package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Olympiad;
import org.springframework.data.repository.CrudRepository;

public interface OlympiadRepository extends CrudRepository<Olympiad, Long> {
    public Olympiad findByYear(int year);
}
