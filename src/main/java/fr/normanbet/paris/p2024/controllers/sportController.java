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


    @GetMapping("/recherche")
    public String rechercheSports(@RequestParam("nomSport") String text, Model model) {
        List<Sport> sportList = sportRepository.findByNameIgnoreCaseStartingWith(text);
        model.addAttribute("sports", sportList);
        return "/gestion/sports";
    }



    @GetMapping
    public String listSports(Model model) {
        List<Sport> sports = (List<Sport>) sportRepository.findAll();
        model.addAttribute("sports", sports);
        return "/gestion/sports";
    }

    @GetMapping("/gestion/ajouterunsport")
    public String showCreateSportForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "/gestion/ajouterunsport";
    }

    @PostMapping("/gestion/ajouterunsport")
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

            redirectAttributes.addFlashAttribute("successMessage", "Sport créé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du sport.");
        }

        return "redirect:/gestion/sports";
    }


    @GetMapping("/update/{id}")
    public String showUpdateSportForm(@PathVariable Long id, Model model) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        model.addAttribute("sport", sport);
        return "/gestion/sport";
    }

    @PostMapping("/update/{id}")
    public String updateSport(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam boolean individual,
                              @RequestParam boolean collective,
                              @RequestParam int sizeTeam) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        sport.setName(name);
        sport.setDescription(description);
        sport.setIndividual(individual);
        sport.setCollective(collective);
        sport.setSizeTeam(sizeTeam);

        sportRepository.save(sport);
        return "redirect:/gestion/sports";
    }


    @GetMapping("/journalisation")
    public String showJournalisation(Model model) {
        List<Journalisation> journalisations = journalisationRepository.findAll();
        model.addAttribute("journalisations", journalisations);
        return "journalisation";
    }

    @DeleteMapping("/journalisation/delete")
    public String deleteFromJournalisation(@RequestParam("selectedItems") List<Long> selectedItems,
                                           RedirectAttributes redirectAttributes) {
        try {
            journalisationRepository.deleteAllById(selectedItems);
            redirectAttributes.addFlashAttribute("successMessage", "Éléments de la journalisation supprimés avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la suppression des éléments de la journalisation.");
        }

        return "redirect:/gestion/sports/journalisation";
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
