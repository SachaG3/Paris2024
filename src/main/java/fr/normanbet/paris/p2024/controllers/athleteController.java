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

    @GetMapping("/athletes")
    public String searchAthletes(@RequestParam(required = false) String query, Model model) {
        List<Athlete> athletes;

        if (query != null && !query.isEmpty()) {
            athletes = athleteRepository.findByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(query, query);
        } else {
            athletes = (List<Athlete>) athleteRepository.findAll();
        }

        model.addAttribute("athletes", athletes);
        model.addAttribute("query", query);
        return "athletes";
    }
}
