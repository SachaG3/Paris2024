package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.*;
import fr.normanbet.paris.p2024.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.normanbet.paris.p2024.services.BetService;

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

    @GetMapping("/{quotationId}")
    public String getQuotation(@PathVariable Long quotationId, Model model) {
        Quotation quotation = quotationRepository.findById(quotationId).orElse(null);
        if (quotation == null) {
            return "redirect:/bet"; // Rediriger si la quotation n'existe pas
        }
        model.addAttribute("quotation", quotation);
        return "/bet/placeBet"; // Le nom de votre template pour placer un pari
    }
    @Autowired
    private BetRepository betRepository;

    @PostMapping("/place")
    public String placeBet(@RequestParam("amount") BigDecimal amount, @RequestParam("quotationId") Long quotationId, @AuthenticationPrincipal User user) {
        Quotation quotation = quotationRepository.findById(quotationId).orElse(null);
        if (quotation == null) {
            // Gérer le cas où la quotation n'est pas trouvée
            return "redirect:/bet";
        }

        Bet newBet = new Bet();
        newBet.setAmount(amount.floatValue());
        newBet.setQuotation(quotation.getQuotation());
        newBet.setOfficialQuotation(quotation);
        newBet.setUser(user);
        newBet.setDateB(LocalDateTime.now());

        betRepository.save(newBet);

        return "redirect:/bet";
    }





    @GetMapping("")
    public String listMatchs(Model model) {
        List<Event> matchsEnCoursEtFuturs = eventRepository.findCurrentAndFutureEvents();
        model.addAttribute("matchs", matchsEnCoursEtFuturs);
        return "/bet/index";
    }


}
