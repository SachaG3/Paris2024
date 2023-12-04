package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Journalisation;
import fr.normanbet.paris.p2024.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalisationRepository extends JpaRepository<Journalisation, Long> {

    // Trouver toutes les journalisations
    List<Journalisation> findAll();

    // Trouver toutes les journalisations associées à un sport spécifique
    List<Journalisation> findAllBySport(Sport sport);

    // Trouver toutes les journalisations ajoutées par un utilisateur spécifique
    List<Journalisation> findAllByAddedBy(String addedBy);

    // Supprimer toutes les journalisations associées à un sport spécifique
    void deleteAllBySport(Sport sport);

    // Supprimer toutes les journalisations ajoutées par un utilisateur spécifique
    void deleteAllByAddedBy(String addedBy);
}
