package fr.normanbet.paris.p2024.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.normanbet.paris.p2024.models.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByOrderByDateNDesc(Pageable pageable);

    List<News> findByTitleContainingIgnoreCase(String search);
}
