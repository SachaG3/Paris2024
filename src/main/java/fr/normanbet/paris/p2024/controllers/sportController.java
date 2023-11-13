package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ajouterunsport")
public class sportController {

    @Autowired
    private SportRepository sportRepository;

    @GetMapping
    public String showCreateSportForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "ajouterunsport";
    }

    @PostMapping
    public String createSport(Sport sport, RedirectAttributes redirectAttributes) {
        try {
            String currentUser = getCurrentUser();
            sport.setAddedBy(currentUser);
            sportRepository.save(sport);
            redirectAttributes.addFlashAttribute("successMessage", "Sport créé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la création du sport.");
        }

        return "redirect:/ajouterunsport";
    }

    @PostMapping("/journalisation")
    public String deleteSelectedSports(@RequestParam("selectedSports") List<Long> selectedSports, RedirectAttributes redirectAttributes) {
        try {
            // Supprimez les sports en fonction de leurs identifiants
            sportRepository.deleteAllById(selectedSports);
            redirectAttributes.addFlashAttribute("successMessage", "Sports supprimés avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la suppression des sports.");
        }

        return "redirect:/ajouterunsport/journalisation";
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/journalisation")
    public String showJournalisation(Model model) {
        List<Sport> sports = (List<Sport>) sportRepository.findAll();
        model.addAttribute("sports", sports);
        return "journalisation";
    }
}
