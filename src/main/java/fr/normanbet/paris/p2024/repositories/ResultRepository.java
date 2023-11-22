package fr.normanbet.paris.p2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ResultRepository<Resultat> extends JpaRepository<Resultat, Long> {
    <Resultat> Resultat findFirstByResult();
}