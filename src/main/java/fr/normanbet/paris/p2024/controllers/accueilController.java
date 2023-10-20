package fr.normanbet.paris.p2024.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class guestController {
    @GetMapping("/")
    public String Accueil(){
        return("Accueil");
    }

}
