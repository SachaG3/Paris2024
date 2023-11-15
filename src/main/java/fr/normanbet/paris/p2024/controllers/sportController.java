package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class sportController {

    @Autowired
    private SportRepository sportRepository;

    @GetMapping("/sports")
    public String searchSports(@RequestParam(required = false) String query, Model model) {
        List<Sport> sports;

        if (query != null && !query.isEmpty()) {
            sports = sportRepository.findByNameOrDescriptionIgnoreCase(query, query);
        } else {
            sports = (List<Sport>) sportRepository.findAll();
        }

        model.addAttribute("sports", sports);
        model.addAttribute("query", query);
        return "sports";
    }
}