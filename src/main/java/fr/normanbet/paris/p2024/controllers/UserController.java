package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Role;
import fr.normanbet.paris.p2024.models.VerificationToken;
import fr.normanbet.paris.p2024.repositories.RoleRepository;
import fr.normanbet.paris.p2024.repositories.VerificationTokenRepository;
import fr.normanbet.paris.p2024.services.DbUserService;
import fr.normanbet.paris.p2024.services.EmailService;
import fr.normanbet.paris.p2024.services.UserService;
import fr.normanbet.paris.p2024.services.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService  verificationTokenService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

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
        user.setActive(false);

        ((DbUserService)uDetailService).encodePassword(user);
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);

            String recipientAddress = user.getEmail();
            String subject = "Confirmation d'inscription";
            String confirmationUrl = "http://localhost:8080/regitrationConfirm?token=" + token;
            String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;

            emailService.sendSimpleMessage(recipientAddress, subject, message);


            redirectAttributes.addFlashAttribute("message", "Utilisateur créé avec succès");
            return "/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'utilisateur");
            return "redirect:/register";
        }
    }
    @GetMapping("/regitrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid token.");
            return "redirect:/badUser";
        }

        User user = verificationToken.getUser();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("message", "Your account is already activated.");
            return "redirect:/login";
        }

        if (verificationToken.getExpiryDate().before(new Date())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your token has expired.");
            return "redirect:/badUser";
        }
        user.setActive(true);

        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Your account has been activated successfully.");
        return "redirect:/login";
    }
    @GetMapping("/user/{login}")
    public String profil(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);
        return "userBoard";

    }



}
