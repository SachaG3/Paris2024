package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class adminpannelController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/gestion/adminpanel")
    public String adminUserPanel(Model model) {
        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);
        return "/gestion/adminpanel";
    }


    @GetMapping("/informationuser/{id}")
    public String showUserInfo(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "informationuser";
    }

    @GetMapping("/deactivate/{id}")
    public String showDeactivateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "deactivate";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateAccount(@PathVariable Long id, String password) {
        User user = userRepository.findById(id).orElse(null);


        if (user != null && user.getRole().getName().equals("admin") && passwordEncoder.matches(password, user.getPassword())) {
            user.setActive(false);
            userRepository.save(user);
        } else if (user != null && (user.getRole().getName().equals("member") || user.getRole().getName().equals("rédacteur"))) {
            user.setActive(false);
            userRepository.save(user);
        }

        return "redirect:/gestion/adminpanel";
    }


    @PostMapping("/activate/{id}")
    public String activateAccount(@PathVariable Long id, String password) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            if (user.getRole().getName().equals("admin") && passwordEncoder.matches(password, user.getPassword())) {
                user.setActive(true);
                userRepository.save(user);
            } else if (user.getRole().getName().equals("member") || user.getRole().getName().equals("rédacteur")) {
                user.setActive(true);
                userRepository.save(user);
            }
        }

        return "redirect:/gestion/adminpanel";
    }


}
