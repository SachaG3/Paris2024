package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.*;
import fr.normanbet.paris.p2024.services.BetService;
import fr.normanbet.paris.p2024.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bets")
public class BetController {

    @Autowired
    private BetService betService;
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public String Bet(Model model){
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        model.addAttribute("upcomingEvents", upcomingEvents);
        return "bet/acceuil";
    }
}

