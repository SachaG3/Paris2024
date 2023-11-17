package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class athleteController {

    @Autowired
    private AthleteRepository athleteRepository;

    @GetMapping("/ajouterunathlete")
    public String showCreateAthleteForm(Model model) {
        model.addAttribute("athlete", new Athlete());
        return "ajouterunathlete";
    }

    @PostMapping("/ajouterunathlete")
    public String createAthlete(Athlete athlete, RedirectAttributes redirectAttributes) {
        try {
            String currentUser = getCurrentUser();
            athlete.setAddedBy(currentUser);
            athleteRepository.save(athlete);
            redirectAttributes.addFlashAttribute("successMessage", "Athlète ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de l'ajout de l'athlète.");
        }

        return "redirect:/ajouterunathlete";
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    @GetMapping("/recherche")
    public String rechercheAthlete(@RequestParam("nomAthlete") String text , Model model) {
        text="%"+text+"%";
        List<Athlete> athleteList = athleteRepository.findByFirstnameLikeOrLastnameLikeAllIgnoreCase(text,text);
        model.addAttribute("athleteList", athleteList);
        return "/athlete";
    }

    @GetMapping("/athlete")
    public String Actualite(Model model) {
        List<Athlete> athleteList = (List<Athlete>) athleteRepository.findAll();
        model.addAttribute("athleteList", athleteList);

        return "/athlete";
    }
}

