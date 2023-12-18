package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Participation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    List<Participation> findByAthlete_Id(Long athleteId);





}
