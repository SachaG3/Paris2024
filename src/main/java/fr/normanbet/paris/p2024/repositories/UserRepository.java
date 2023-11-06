package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByLogin(String login);
    public Optional<User> findByEmail(String email);


    public Optional<User> findById(Long id);

    public List<User> findByLoginLikeOrEmailLike(String login, String email);

    @Query("Select u FROM User u WHERE u.login like :login OR u.email like :email")
    public List<User> filter(String login, String email);
}
