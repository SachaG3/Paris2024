package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Sport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SportRepository extends CrudRepository<Sport, Long> {
    public Sport findFirstByNameOrDescriptionIgnoreCase(String name,String description);

    List<Sport> findByNameOrDescriptionIgnoreCase(String name);
}
