package fr.normanbet.paris.p2024.advices;

import ch.qos.logback.core.Layout;
import com.samskivert.mustache.Mustache;
import fr.normanbet.paris.p2024.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.Set;

@ControllerAdvice
class MainAdvice {
    private final Mustache.Compiler compiler;

    @Autowired
    public MainAdvice(Mustache.Compiler compiler) {
        this.compiler = compiler;
    }

    @ModelAttribute("activeUser")
    public User activeUser(Authentication auth) {
        return (auth == null) ? null : (User) auth.getPrincipal();
    }
    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            return user.getId() == 2; // Vérifie si l'ID de l'utilisateur est 2
        }

        return false;
    }

}
