package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.exceptions.DepositLimitExceededException;
import fr.normanbet.paris.p2024.exceptions.InsufficientFundsException;
import fr.normanbet.paris.p2024.models.Operation;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.types.OperationType;
import fr.normanbet.paris.p2024.repositories.OperationRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Data
public class OperationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void withdraw(User user, BigDecimal amount) {
        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        Operation withdrawOperation = new Operation();
        withdrawOperation.setUser(user);
        withdrawOperation.setAmount(amount);
        withdrawOperation.setType(OperationType.WITHDRAWAL);
        withdrawOperation.setDateO(LocalDateTime.now());
        operationRepository.save(withdrawOperation);
    }

    @Transactional
    public void deposit(User user, BigDecimal amount) {
        if (userService.checkDepositLimit(user, amount)) {
            // La limite de dépôt n'est pas dépassée, continuez avec le dépôt
            Operation deposit = new Operation();
            deposit.setUser(user);
            deposit.setAmount(amount);
            deposit.setType(OperationType.DEPOSIT);
            deposit.setDateO(LocalDateTime.now());
            operationRepository.save(deposit);

            // Mettez à jour le solde de l'utilisateur
            user.setBalance(user.getBalance().add(amount));
            userRepository.save(user);
        } else {
            throw new DepositLimitExceededException();
        }
    }
    public List<Operation> findTop5OperationsByUser(User user) {
        return operationRepository.findTop5ByUserOrderByDateODesc(user);
    }

    public List<Operation> findAllOperationsByUser(User user) {
        return operationRepository.findByUserOrderByDateODesc(user);
    }
}

