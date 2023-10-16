package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Country;
import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.models.types.DisciplineType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DisciplineRepository extends CrudRepository<Discipline, Long> {
}
