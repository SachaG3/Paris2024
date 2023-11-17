package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.models.Journalisation;
import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.models.types.DisciplineType;
import fr.normanbet.paris.p2024.repositories.DisciplineRepository;
import fr.normanbet.paris.p2024.repositories.JournalisationRepository;
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

    @Autowired
    private JournalisationRepository journalisationRepository;

    @Autowired
    private DisciplineRepository disciplineRepository; // Assurez-vous d'avoir correctement injecté votre repository de disciplines

    @GetMapping
    public String showCreateSportForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "ajouterunsport";
    }

    @PostMapping
    public String createSport(@ModelAttribute Sport sport,
                              @RequestParam(name = "discipline", required = false) String discipline,
                              RedirectAttributes redirectAttributes) {
        try {
            String currentUser = getCurrentUser();
            sport.setAddedBy(currentUser);
            sportRepository.save(sport);

            // Si des disciplines sont fournies, les enregistrer
            if (discipline != null && !discipline.isEmpty()) {
                String[] disciplineArray = discipline.split(",");
                for (String disciplineName : disciplineArray) {
                    Discipline newDiscipline = new Discipline();
                    newDiscipline.setName(disciplineName.trim()); // Supprimez les espaces autour de la discipline
                    newDiscipline.setType(DisciplineType.MIXTE); // Mettez le type de discipline approprié ici
                    newDiscipline.setSport(sport);
                    disciplineRepository.save(newDiscipline);
                }
            }

            // Ajouter la journalisation
            Journalisation journalisation = new Journalisation();
            journalisation.setSport(sport);
            journalisation.setAddedBy(currentUser);
            journalisationRepository.save(journalisation);

            redirectAttributes.addFlashAttribute("successMessage", "Sport créé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la création du sport.");
        }

        return "redirect:/ajouterunsport";
    }

    @GetMapping("/journalisation")
    public String showJournalisation(Model model) {
        List<Journalisation> journalisations = (List<Journalisation>) journalisationRepository.findAll();
        model.addAttribute("journalisations", journalisations);
        return "journalisation";
    }

    @PostMapping("/journalisation/delete")
    public String deleteFromJournalisation(@RequestParam("selectedItems") List<Long> selectedItems, RedirectAttributes redirectAttributes) {
        try {
            // Supprimez les éléments de la journalisation en fonction de leurs identifiants
            journalisationRepository.deleteAllById(selectedItems);
            redirectAttributes.addFlashAttribute("successMessage", "Éléments de la journalisation supprimés avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la suppression des éléments de la journalisation.");
        }

        return "redirect:/ajouterunsport/journalisation";
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
