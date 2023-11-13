package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.models.Operation;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.types.OperationType;
import fr.normanbet.paris.p2024.repositories.OperationRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Data
public class OperationService {

    private final UserRepository userRepository;
    private final OperationRepository operationRepository;

    @Autowired
    public OperationService(UserRepository userRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }

    public void deposit(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        Operation depositOperation = new Operation();
        depositOperation.setUser(user);
        depositOperation.setAmount(amount);
        depositOperation.setType(OperationType.DEPOSIT);
        depositOperation.setDateO(LocalDateTime.now());
        operationRepository.save(depositOperation);
    }

    public void withdraw(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        Operation withdrawOperation = new Operation();
        withdrawOperation.setUser(user);
        withdrawOperation.setAmount(amount);
        withdrawOperation.setType(OperationType.WITHDRAWAL);
        withdrawOperation.setDateO(LocalDateTime.now());
        operationRepository.save(withdrawOperation);
    }
}

