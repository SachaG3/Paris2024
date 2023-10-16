package fr.normanbet.paris.p2024.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class actualiteController {
    @GetMapping("/actualite")
    public String Actualite(){


        return("Actualite");
        }

}

