package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role getOne(long l);
}
