package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import fr.normanbet.paris.p2024.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/rechercheSportsAthletes")
public class rechercheSportAthleteController {

    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private SportRepository sportRepository;

    @GetMapping
    public String searchSportsAthletes(@RequestParam(name = "q", required = false) String query, Model model) {
        List<Athlete> athleteList = athleteRepository.findByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(query, query);
        List<Sport> sportList = sportRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        model.addAttribute("athleteList", athleteList);
        model.addAttribute("sportList", sportList);

        return "rechercheSportsAthletes";
    }
}
