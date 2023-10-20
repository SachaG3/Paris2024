package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class athleteController {

    @Autowired
    private AthleteRepository athleteRepository;

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
