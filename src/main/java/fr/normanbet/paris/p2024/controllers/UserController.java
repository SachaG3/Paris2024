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
    public String createUserSubmit(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {


        Optional<User> existingUserLogin = userRepository.findByLogin(user.getLogin());
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserLogin.isPresent()||existingUserEmail.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Ce login ou Email est déjà utilisé par un autre utilisateur");
            return "/register";
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
            model.addAttribute("userId", savedUser.getId());
            System.out.println(savedUser.getId());
            return "/mailvalidation";
        } else {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'utilisateur");
            return "/register";
        }
    }

    @PostMapping("/resendValidationEmail")
    public String resendValidationEmail(@RequestParam Long userId, RedirectAttributes redirectAttributes,Model model) {
        System.out.println("passer");
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && !user.isActive()) {
            System.out.println("passer 2");
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);

            String recipientAddress = user.getEmail();
            String subject = "Confirmation d'inscription - Renvoi";
            String confirmationUrl = "http://localhost:8080/regitrationConfirm?token=" + token;
            String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;

            emailService.sendSimpleMessage(recipientAddress, subject, message);

            redirectAttributes.addFlashAttribute("message", "Un nouvel e-mail de confirmation a été envoyé.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Impossible de renvoyer l'e-mail de confirmation.");
        }
        model.addAttribute("userId",user.getId());
        return "/mailvalidation";
    }
    @PostMapping("/resendBadValidationEmail")
    public String resendValidationEmail(@RequestParam String email, RedirectAttributes redirectAttributes,Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Aucun compte trouvé avec cet e-mail.");
            return "/badUser";
        }

        User user = userOptional.get();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ce compte est déjà activé.");
            return "/login";
        }

        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Confirmation d'inscription - Renvoi";
        String confirmationUrl = "http://localhost:8080/regitrationConfirm?token=" + token;
        String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;
        emailService.sendSimpleMessage(recipientAddress, subject, message);

        redirectAttributes.addFlashAttribute("message", "Un e-mail de validation a été renvoyé.");
        model.addAttribute("userId",user.getId());
        return "/mailvalidation";
    }
    @GetMapping("/regitrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            System.out.println("test");
            redirectAttributes.addFlashAttribute("errorMessage", "Mauvais token.");
            return "/badUser";
        }

        User user = verificationToken.getUser();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("message", "Compte déjà activé.");
            return "/login";
        }

        if (verificationToken.getExpiryDate().before(new Date())) {
            redirectAttributes.addFlashAttribute("errorMessage", "le token à été expiré.");
            return "/badUser";
        }
        user.setActive(true);

        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Compte activé.");
        return "/login";
    }

    @GetMapping("/user/{login}")
    public String profil(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);
        return "userBoard";

    }



}
