package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.services.DbUserService;
import fr.normanbet.paris.p2024.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService uDetailService;


    @GetMapping("register")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    public String createUserSubmit(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        ((DbUserService)uDetailService).encodePassword(user);

        User savedUser = userRepository.save(user);

        if (savedUser != null) {
            redirectAttributes.addFlashAttribute("message", "Utilisateur créé avec succès");
            return "redirect:/users/" + savedUser.getId();
        } else {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'utilisateur");
            return "redirect:/register";
        }
    }

}
