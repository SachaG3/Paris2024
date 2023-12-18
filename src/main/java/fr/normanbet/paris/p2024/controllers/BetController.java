package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.*;
import fr.normanbet.paris.p2024.repositories.*;
import fr.normanbet.paris.p2024.services.OperationService;
import fr.normanbet.paris.p2024.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bet")
public class BetController {

    @Autowired
    private EventRepository eventRepository;
    // Dans BetController

    @Autowired
    private QuotationRepository quotationRepository; // Assurez-vous que ce repository est bien créé
    @Autowired
    private UserService userService; // Assurez-vous que ce service est bien configuré
    @Autowired
    private OperationService operationService;


    @GetMapping("/{quotationId}")
    public String getQuotation(@PathVariable Long quotationId, Model model) {
        Quotation quotation = quotationRepository.findById(quotationId).orElse(null);
        if (quotation == null) {
            return "redirect:/bet";
        }
        model.addAttribute("quotation", quotation);
        return "/bet/placeBet";
    }
    @Autowired
    private BetRepository betRepository;


    @PostMapping("/place")
    public String placeBet(@RequestParam("amount") BigDecimal amount, @RequestParam("quotationId") Long quotationId, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        Quotation quotation = quotationRepository.findById(quotationId).orElse(null);
        if (quotation == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Quotation non trouvée.");
            return "redirect:/bet";
        }
        if (user.getBalance().compareTo(amount) >= 0) {
            Bet newBet = new Bet();
            newBet.setAmount(amount);
            newBet.setQuotation(quotation.getQuotation());
            newBet.setOfficialQuotation(quotation);
            newBet.setUser(user);
            newBet.setDateB(LocalDateTime.now());
            betRepository.save(newBet);

            operationService.withdraw(user, amount);
            redirectAttributes.addFlashAttribute("successMessage", "Pari placé avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Fonds insuffisants pour placer le pari.");
        }

        return "redirect:/bet";
    }
    @GetMapping("")
    public String listMatchs(Model model) {
        List<Event> matchsEnCoursEtFuturs = eventRepository.findCurrentAndFutureEvents();
        model.addAttribute("matchs", matchsEnCoursEtFuturs);
        return "/bet/index";
    }


}
