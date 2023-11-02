package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Sport;
import org.springframework.data.repository.CrudRepository;

public interface SportRepository extends CrudRepository<Sport, Long> {
    public Sport findFirstByNameOrDescriptionIgnoreCase(String name,String description);

}
