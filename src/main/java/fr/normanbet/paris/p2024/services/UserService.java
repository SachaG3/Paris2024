package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.exceptions.InvalidUserException;
import fr.normanbet.paris.p2024.exceptions.UserNotFoundException;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository utilisateurRepo;

    @Autowired
    private UserDetailsService uDetailService;

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

}
