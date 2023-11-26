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
@RequestMapping("/sports")
public class sportController {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private JournalisationRepository journalisationRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @GetMapping
    public String listSports(Model model) {
        List<Sport> sports = (List<Sport>) sportRepository.findAll();
        model.addAttribute("sports", sports);
        return "sports";
    }

    @GetMapping("/create")
    public String showCreateSportForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "ajouterunsport";
    }

    @PostMapping("/create")
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
                    newDiscipline.setName(disciplineName.trim());
                    newDiscipline.setType(DisciplineType.MIXTE);
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

        return "redirect:/sports/create";
    }

    @GetMapping("/update/{id}")
    public String showUpdateSportForm(@PathVariable Long id, Model model) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        model.addAttribute("sport", sport);
        return "modifierunsport";
    }

    @PostMapping("/update/{id}")
    public String updateSport(@PathVariable Long id,
                              @ModelAttribute Sport updatedSport,
                              RedirectAttributes redirectAttributes) {
        try {
            Sport sport = sportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sport not found"));

            sport.setName(updatedSport.getName());
            sport.setDescription(updatedSport.getDescription());
            sport.setIndividual(updatedSport.isIndividual());
            sport.setCollective(updatedSport.isCollective());
            sport.setSizeTeam(updatedSport.getSizeTeam());

            sportRepository.save(sport);
            redirectAttributes.addFlashAttribute("successMessage", "Sport mis à jour avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour du sport.");
        }

        return "redirect:/sports/update/" + id;
    }

    @GetMapping("/journalisation")
    public String showJournalisation(Model model) {
        List<Journalisation> journalisations = journalisationRepository.findAll();
        model.addAttribute("journalisations", journalisations);
        return "journalisation";
    }

    @PostMapping("/journalisation/delete")
    public String deleteFromJournalisation(@RequestParam("selectedItems") List<Long> selectedItems,
                                           RedirectAttributes redirectAttributes) {
        try {
            journalisationRepository.deleteAllById(selectedItems);
            redirectAttributes.addFlashAttribute("successMessage", "Éléments de la journalisation supprimés avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Une erreur s'est produite lors de la suppression des éléments de la journalisation.");
        }

        return "redirect:/sports/journalisation";
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
