package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Operation;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.OperationRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import fr.normanbet.paris.p2024.services.OperationService;
import fr.normanbet.paris.p2024.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@Getter
@Setter
@RequestMapping("/portefeuille")
public class portefeuilleController {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private OperationService operationService;
    @Autowired
    private UserService userService;


    @GetMapping("")
    public String PortefeuillePage(@AuthenticationPrincipal User user, Model model) {
        List<Operation> recentOperations = operationRepository.findTop10ByUserOrderByDateODesc(user);
        model.addAttribute("balance", user.getBalance());
        model.addAttribute("recentOperations", recentOperations);
        return "portefeuille/portefeuille";
    }
    @GetMapping("/depot")
    public String showDepositView(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("balance", user.getBalance());
        return "portefeuille/depot";
    }

    @GetMapping("/retrait")
    public String showWithdrawView(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("balance", user.getBalance());
        return "portefeuille/retrait";
    }
    @PostMapping("/depot")
    public String handleDeposit(@AuthenticationPrincipal User user,
                                @RequestParam("amount") BigDecimal amount,
                                RedirectAttributes redirectAttributes) {
        try {
            operationService.deposit(user, amount);
            redirectAttributes.addFlashAttribute("successMessage", "Dépôt réussi de " + amount + " €");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/portefeuille";
    }

    @PostMapping("/retrait")
    public String handleWithdraw(@AuthenticationPrincipal User user,
                                 @RequestParam("amount") BigDecimal amount,
                                 RedirectAttributes redirectAttributes) {
        try {
            operationService.withdraw(user, amount);
            redirectAttributes.addFlashAttribute("successMessage", "Retrait réussi de " + amount + " €");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du retrait: " + e.getMessage());
        }
        return "redirect:/portefeuille";
    }

    @GetMapping("/operation")
    public String getAllOperationsPage(@AuthenticationPrincipal User user, Model model) {
        List<Operation> allOperations = operationRepository.findByUserOrderByDateODesc(user);
        model.addAttribute("allOperations", allOperations);
        return "portefeuille/operation";
    }

    @PostMapping("/limite")
    public String setDepositLimit(@AuthenticationPrincipal User user,
                                  @RequestParam("limit") BigDecimal limit,
                                  @RequestParam("period") String period,
                                  RedirectAttributes redirectAttributes) {
        // Logique pour définir la limite de dépôt
        try {
            userService.setDepositLimit(user, limit, period);
            redirectAttributes.addFlashAttribute("successMessage", "Limite de dépôt définie avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la définition de la limite: " + e.getMessage());
        }
        return "redirect:/portefeuille";
    }
    @GetMapping("/limite")
    public String limite(@AuthenticationPrincipal User user,Model model){
        model.addAttribute("limite", user.getDepositLimit());
        return "portefeuille/limite";
    }


}

