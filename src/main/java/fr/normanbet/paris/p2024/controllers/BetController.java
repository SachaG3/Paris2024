package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.*;
import fr.normanbet.paris.p2024.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bets")
public class BetController {

    @Autowired
    private BetService betService;


    @GetMapping("")
    public String Bet(){
        return "bet/acceuil";
    }
}

