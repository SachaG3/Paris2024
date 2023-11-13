package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import fr.normanbet.paris.p2024.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class rechercheSportAthleteController {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @GetMapping("/recherche")
    public void sportsActualites(Model model) {
        List<Sport> sportList = (List<Sport>) sportRepository.findAll();
        List<Athlete> athleteList = (List<Athlete>) athleteRepository.findAll();
    }

    @PostMapping("/recherche")
    public String sportsSearchAction(@RequestParam("nomSport") String text, Model model) {
        text = "%" + text + "%";
        List<Sport> sportList = sportRepository.findByNameOrDescriptionIgnoreCase(text);
        model.addAttribute("sportList", sportList);
        List<Athlete> athleteList = (List<Athlete>) athleteRepository.findAll();
        model.addAttribute("athleteList", athleteList);
        return "rechercher";
    }
}