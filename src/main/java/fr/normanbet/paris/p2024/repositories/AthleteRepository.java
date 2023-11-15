package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Athlete;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AthleteRepository extends CrudRepository<Athlete, Long> {
    List<Athlete> findByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(String firstname, String lastname);
}
