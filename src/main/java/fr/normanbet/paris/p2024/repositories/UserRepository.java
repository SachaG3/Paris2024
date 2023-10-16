package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
