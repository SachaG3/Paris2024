package fr.normanbet.paris.p2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.normanbet.paris.p2024.models.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

}
