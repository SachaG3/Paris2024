package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.models.types.GenreType;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/athletes")
public class athleteController {

    @Autowired
    private AthleteRepository athleteRepository;

    @GetMapping("/recherche")
    public String rechercheAthlete(@RequestParam("nomAthlete") String text, Model model) {
        text = "%" + text + "%";
        List<Athlete> athleteList = athleteRepository.findByFirstnameLikeOrLastnameLikeAllIgnoreCase(text, text);
        model.addAttribute("athleteList", athleteList);
        return "athlete";
    }

    @GetMapping("")
    public String actualite(Model model) {
        List<Athlete> athleteList = (List<Athlete>) athleteRepository.findAll();
        model.addAttribute("athleteList", athleteList);
        return "athlete";
    }

    @PostMapping("/update/{id}")
    public String updateAthlete(@PathVariable Long id,
                                @RequestParam String firstname,
                                @RequestParam String lastname,
                                @RequestParam String birthdate,
                                @RequestParam String genre,
                                Model model) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete not found"));

        athlete.setFirstname(firstname);
        athlete.setLastname(lastname);
        athlete.setBirthdate(LocalDate.parse(birthdate));
        athlete.setGenre(GenreType.valueOf(genre));

        athleteRepository.save(athlete);
        model.addAttribute("successMessage", "Athlete mis à jour avec succès !");
        return "athlete";
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAthlete(@PathVariable Long id) {
        athleteRepository.deleteById(id);
    }

    @GetMapping("/modifierunathlete/{id}")
    public String modifierAthlete(@PathVariable Long id, Model model) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete not found"));
        model.addAttribute("athlete", athlete);
        return "modifierunathlete";
    }
}
