package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            sportRepository.save(sport);
            redirectAttributes.addFlashAttribute("successMessage", "Sport créé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la création du sport.");
        }

        return "redirect:/ajouterunsport";
    }

    @GetMapping("/journalisation")
    public String showJournalisation(Model model) {
        List<Sport> sports = (List<Sport>) sportRepository.findAll();
        model.addAttribute("sports", sports);
        return "journalisation";
    }

}
