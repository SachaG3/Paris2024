package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Country;
import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.models.Quotation;
import fr.normanbet.paris.p2024.models.QuotationIndividual;
import fr.normanbet.paris.p2024.models.QuotationTeam;
import fr.normanbet.paris.p2024.repositories.CountryRepository; // Assurez-vous d'avoir un repository pour Country
import fr.normanbet.paris.p2024.repositories.EventRepository;
import fr.normanbet.paris.p2024.repositories.QuotationIndividualRepository;
import fr.normanbet.paris.p2024.repositories.QuotationRepository;
import fr.normanbet.paris.p2024.repositories.QuotationTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class resultatController {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationIndividualRepository quotationIndividualRepository;

    @Autowired
    private QuotationTeamRepository quotationTeamRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/resultats")
    public String afficherResultats(Model model) {
        List<Quotation> resultats = quotationRepository.findAll();
        List<QuotationIndividual> resultatsIndividual = quotationIndividualRepository.findAll();
        List<QuotationTeam> resultatsTeam = quotationTeamRepository.findAll();
        List<Event> events = eventRepository.findAll();
        List<Country> countries = (List<Country>) countryRepository.findAll(); // Ajoutez cette ligne pour récupérer les pays

        model.addAttribute("resultats", resultats);
        model.addAttribute("resultatsIndividual", resultatsIndividual);
        model.addAttribute("resultatsTeam", resultatsTeam);
        model.addAttribute("events", events);
        model.addAttribute("countries", countries); // Ajoutez cette ligne pour ajouter les pays

        return "resultat";
    }
}
