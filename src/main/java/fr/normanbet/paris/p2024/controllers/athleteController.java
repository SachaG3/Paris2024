package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.models.Participation;
import fr.normanbet.paris.p2024.models.types.GenreType;
import fr.normanbet.paris.p2024.repositories.AthleteRepository;
import fr.normanbet.paris.p2024.repositories.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/athlete")
public class athleteController {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private ParticipationRepository participationRepository;
    @GetMapping("/ficheathlete/{id}")
    public String afficherFicheAthlete(@PathVariable Long id, Model model) {
        Optional<Participation> participation = participationRepository.findByAthlete_Id(id).stream().findFirst();

        if (participation.isPresent()) {
            Participation participationEntity = participation.get();
            model.addAttribute("participation", participationEntity);
            model.addAttribute("athlete", participationEntity.getAthlete());
            model.addAttribute("discipline", participationEntity.getDiscipline());
            model.addAttribute("country", participationEntity.getCountry());
            model.addAttribute("olympiad", participationEntity.getOlympiad());
            return "fiche-athlete";
        } else {
            throw new RuntimeException("Participation not found for athlete with id: " + id);
        }
    }













}
