package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Role;
import fr.normanbet.paris.p2024.repositories.RoleRepository;
import fr.normanbet.paris.p2024.services.DbUserService;
import fr.normanbet.paris.p2024.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService uDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository ;
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(@RequestParam String login, @RequestParam String password, Model model) {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "redirect:/";
            }
        }
        return "login";
    }
    @GetMapping("register")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    public String createUserSubmit(@ModelAttribute User user, RedirectAttributes redirectAttributes) {


        Optional<User> existingUserLogin = userRepository.findByLogin(user.getLogin());
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserLogin.isPresent()||existingUserEmail.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Ce login ou Email est déjà utilisé par un autre utilisateur");
            return "redirect:/register";
        }

        Role role = roleRepository.getOne(1L);
        user.setRole(role);

        ((DbUserService)uDetailService).encodePassword(user);
        User savedUser = userRepository.save(user);






        if (savedUser != null) {
            redirectAttributes.addFlashAttribute("message", "Utilisateur créé avec succès");
            return "redirect:";
        } else {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'utilisateur");
            return "redirect:/register";
        }
    }
    @GetMapping("/user/{login}")
    public String profil(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);
        return "userBoard";

    }



}
