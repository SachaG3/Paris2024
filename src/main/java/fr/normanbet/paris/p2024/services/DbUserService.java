package fr.normanbet.paris.p2024.services;

import java.util.Optional;

import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public class DbUserService implements UserDetailsService {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private PasswordEncoder pEncoder;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> optUser = uRepo.findByLogin(login);
        return optUser.orElse(null);
    }
    public void encodePassword(User user) {
        user.setPassword(pEncoder.encode(user.getPassword()));
    }
}
