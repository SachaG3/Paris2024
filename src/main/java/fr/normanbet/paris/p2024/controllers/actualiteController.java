package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.News;
import fr.normanbet.paris.p2024.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
public class actualiteController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/actualite")
    public String Actualite(Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);

        return "/actualite";
    }
}
