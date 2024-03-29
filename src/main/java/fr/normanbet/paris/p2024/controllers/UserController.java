package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Role;
import fr.normanbet.paris.p2024.models.VerificationToken;
import fr.normanbet.paris.p2024.repositories.BetRepository;
import fr.normanbet.paris.p2024.repositories.RoleRepository;
import fr.normanbet.paris.p2024.repositories.VerificationTokenRepository;
import fr.normanbet.paris.p2024.services.DbUserService;
import fr.normanbet.paris.p2024.services.EmailService;
import fr.normanbet.paris.p2024.services.UserService;
import fr.normanbet.paris.p2024.services.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.UserRepository;

import java.math.BigDecimal;
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
    @Autowired
    private BetRepository betRepository;

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }
    @PostMapping("/login")
    public String handleLogin(@RequestParam String login, @RequestParam String password, Model model) {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "/";
            }
        }
        return "/user/login";
    }
    @GetMapping("register")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/user/register";
    }

    @PostMapping("register")
    public String createUserSubmit(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {


        Optional<User> existingUserLogin = userRepository.findByLogin(user.getLogin());
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserLogin.isPresent()||existingUserEmail.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Ce login ou Email est déjà utilisé par un autre utilisateur");
            return "/user/register";
        }

        Role role = roleRepository.getById(1);
        user.setRole(role);
        user.setActive(false);


        ((DbUserService)uDetailService).encodePassword(user);
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);

            String recipientAddress = user.getEmail();
            String subject = "Confirmation d'inscription";
            String confirmationUrl = "http://srv2-vm-2116.sts-sio-caen.info/regitrationConfirm?token=" + token;
            String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;

            emailService.sendSimpleMessage(recipientAddress, subject, message);


            redirectAttributes.addFlashAttribute("message", "Utilisateur créé avec succès");
            model.addAttribute("userId", savedUser.getId());
            System.out.println(savedUser.getId());
            return "/user/mailvalidation";
        } else {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'utilisateur");
            return "/user/register";
        }
    }

    @PostMapping("/resendValidationEmail")
    public String resendValidationEmail(@RequestParam Long userId, RedirectAttributes redirectAttributes,Model model) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && !user.isActive()) {
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);

            String recipientAddress = user.getEmail();
            String subject = "Confirmation d'inscription - Renvoi";
            String confirmationUrl = "http://srv2-vm-2116.sts-sio-caen.info/regitrationConfirm?token=" + token;
            String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;

            emailService.sendSimpleMessage(recipientAddress, subject, message);

            redirectAttributes.addFlashAttribute("message", "Un nouvel e-mail de confirmation a été envoyé.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Impossible de renvoyer l'e-mail de confirmation.");
        }
        model.addAttribute("userId",user.getId());
        return "/user/mailvalidation";
    }
    @PostMapping("/resendBadValidationEmail")
    public String resendValidationEmail(@RequestParam String email, RedirectAttributes redirectAttributes,Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Aucun compte trouvé avec cet e-mail.");
            return "/user/badUser";
        }

        User user = userOptional.get();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ce compte est déjà activé.");
            return "/user/login";
        }

        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Confirmation d'inscription - Renvoi";
        String confirmationUrl = "http://srv2-vm-2116.sts-sio-caen.info/regitrationConfirm?token=" + token;
        String message = "Pour confirmer votre inscription, veuillez ouvrir le lien suivant : " + confirmationUrl;
        emailService.sendSimpleMessage(recipientAddress, subject, message);

        redirectAttributes.addFlashAttribute("message", "Un e-mail de validation a été renvoyé.");
        model.addAttribute("userId",user.getId());
        return "/user/mailvalidation";
    }
    @GetMapping("/regitrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mauvais token.");
            return "/user/badUser";
        }

        User user = verificationToken.getUser();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("message", "Compte déjà activé.");
            return "/user/login";
        }

        if (verificationToken.getExpiryDate().before(new Date())) {
            redirectAttributes.addFlashAttribute("errorMessage", "le token à été expiré.");
            return "/user/badUser";
        }
        user.setActive(true);

        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Compte activé.");
        return "/user/login";
    }

    @GetMapping("/user/{login}")
    public String profil(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);
        return "/user/userBoard";

    }
    @PostMapping("/user/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur a un solde positif
        if (user.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de désactiver le compte car il reste un solde.");
            return "/user/profiles";
        }

        // Vérifier si l'utilisateur a des paris actifs
        if (betRepository.existsByUserAndIsActive(user, true)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de désactiver le compte car il y a des paris en cours.");
            return "/user/profiles";
        }

        // Désactiver le compte
        user.setActive(false);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Votre compte a été désactivé.");
        return "/logout";
    }




}

