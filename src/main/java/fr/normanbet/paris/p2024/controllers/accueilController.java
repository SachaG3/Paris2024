package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.News;
import fr.normanbet.paris.p2024.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<News> element1 = newsRepository.findAllByOrderByDateNDesc(PageRequest.of(0, 1));
        List<News> newsList = newsRepository.findAllByOrderByDateNDesc(PageRequest.of(1, 5));
        model.addAttribute("newsList", newsList);
        model.addAttribute("element1", element1);

        return("Accueil");
    }

}
