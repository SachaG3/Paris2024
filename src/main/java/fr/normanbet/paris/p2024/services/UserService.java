package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.exceptions.InvalidUserException;
import fr.normanbet.paris.p2024.exceptions.UserNotFoundException;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.VerificationToken;
import fr.normanbet.paris.p2024.models.types.OperationType;
import fr.normanbet.paris.p2024.repositories.OperationRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import fr.normanbet.paris.p2024.repositories.VerificationTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository utilisateurRepo;

    @Autowired
    private UserDetailsService uDetailService;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    private final UserRepository userRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public void createVerificationToken(User user, String newToken) {
        Optional<VerificationToken> existingToken = tokenRepository.findByUserId(user.getId());
        existingToken.ifPresent(token -> tokenRepository.delete(token));

        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken(newToken);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        myToken.setExpiryDate(cal.getTime());

        tokenRepository.save(myToken);
    }


    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public User findByLogin(String login) throws InvalidUserException {
        Optional<User> opt = utilisateurRepo.findByLogin(login);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new InvalidUserException();
    }

    public User getById(Long id) throws UserNotFoundException {
        Optional<User> opt = utilisateurRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserNotFoundException("Utilisateur d'identifiant " + id + " non trouvé !");
    }

    public User createUser(String login,String password) {
        User u=new User();
        u.setLogin(login);
        u.setEmail(login+"@caensup.fr");
        u.setPassword(password);
        ((DbUserService)uDetailService).encodePassword(u);
        return utilisateurRepo.save(u);
    }
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null && verificationToken.getExpiryDate().after(new Date())) {
            User user = verificationToken.getUser();
            user.isEnabled();
            utilisateurRepo.save(user);
            return "valid";
        }
        return "invalid";
    }
    public boolean isUserEnabled(String username) {
        Optional<User> optionalUser = utilisateurRepo.findByLogin(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get().isEnabled();
        }
        return false;
    }
    public void setDepositLimit(User user, BigDecimal limit, String period) {
        user.setDepositLimit(limit);
        user.setDepositLimitPeriod(period);
        userRepository.save(user);
    }

    public boolean checkDepositLimit(User user, BigDecimal depositAmount) {
        BigDecimal limit = user.getDepositLimit();
        String period = user.getDepositLimitPeriod();
        LocalDateTime since;

        // Déterminez la période de temps pour la limite
        if ("daily".equals(period)) {
            since = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if ("weekly".equals(period)) {
            since = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else {
            // Si la période n'est pas définie ou incorrecte, vous pouvez lancer une exception ou gérer comme vous le souhaitez
            throw new IllegalArgumentException("Période de limite de dépôt incorrecte.");
        }

        // Calculez le total des dépôts depuis le début de la période
        BigDecimal totalDeposits = operationRepository.findSumOfDepositsForUserSince(user, OperationType.DEPOSIT, since);

        // Si totalDeposits est null, aucun dépôt n'a été effectué depuis 'since'
        if (totalDeposits == null) totalDeposits = BigDecimal.ZERO;

        // Vérifiez si le dépôt actuel dépasse la limite
        return totalDeposits.add(depositAmount).compareTo(limit) <= 0;
    }
}
