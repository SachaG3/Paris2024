package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Resultat;
import fr.normanbet.paris.p2024.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class resultatController {

    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/resultat")
    public String afficherResultats(Model model) {
        List resultats = resultRepository.findAll();
        model.addAttribute("resultat", resultat);
        return "resultat"; // Cela correspond au nom de votre fichier HTML
    }
}
