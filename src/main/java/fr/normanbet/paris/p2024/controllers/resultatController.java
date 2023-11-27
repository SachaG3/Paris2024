package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Quotation;  // Assurez-vous que c'est la classe correcte
import fr.normanbet.paris.p2024.repositories.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class resultatController {

    @Autowired
    private QuotationRepository quotationRepository;

    @GetMapping("/resultats")
    public String afficherResultats(Model model) {
        List<Quotation> resultats = quotationRepository.findAll(); // Utilisez la méthode appropriée selon vos besoins

        model.addAttribute("resultats", resultats);

        return "resultat";  // Ceci correspond au nom de votre fichier resultat.html
    }
}
