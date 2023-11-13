package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Operation;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.OperationRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import fr.normanbet.paris.p2024.services.OperationService;
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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du dépôt: " + e.getMessage());
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
        List<Operation> allOperations = operationRepository.findByUser(user);
        model.addAttribute("allOperations", allOperations);
        return "portefeuille/operation";
    }

}

