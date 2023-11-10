package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.exceptions.InvalidUserException;
import fr.normanbet.paris.p2024.exceptions.UserNotFoundException;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.VerificationToken;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import fr.normanbet.paris.p2024.repositories.VerificationTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken(token);

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
}
