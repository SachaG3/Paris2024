package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.News;
import fr.normanbet.paris.p2024.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class accueilController {
    @Autowired
    private NewsRepository newsRepository;
    @GetMapping("/")
    public String Accueil(Model model){
        List<News> element1 = newsRepository.findAllByOrderByDateNDesc(PageRequest.of(0, 6));
        model.addAttribute("element1", element1.get(0));
        model.addAttribute("element2", element1.get(1));
        model.addAttribute("element3", element1.get(2));
        model.addAttribute("element4", element1.get(3));
        model.addAttribute("element5", element1.get(4));

        return("Accueil");
    }

}
