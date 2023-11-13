package fr.normanbet.paris.p2024.repositories;

import fr.normanbet.paris.p2024.models.Operation;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.types.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByUser(User user);

    List<Operation> findByUserAndType(User user, OperationType type);
    List<Operation> findTop10ByUserOrderByDateODesc(User user);
    List<Operation> findByUserOrderByDateODesc(User user);
    @Query("SELECT SUM(o.amount) FROM Operation o WHERE o.user = :user AND o.type = :type AND o.dateO >= :since")
    BigDecimal findSumOfDepositsForUserSince(User user, OperationType type, LocalDateTime since);

}
