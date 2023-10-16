package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
    public Country findFirstByNameIgnoreCaseOrNotesContainingIgnoreCase(String name,String notes);
}
