package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Event;
import fr.normanbet.paris.p2024.models.News;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.repositories.EventRepository;
import fr.normanbet.paris.p2024.repositories.NewsRepository;
import fr.normanbet.paris.p2024.repositories.UserRepository;
import fr.normanbet.paris.p2024.services.DbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes( "user")
public class accueilController {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository uRepository;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/")
    public String Accueil(Model model, @AuthenticationPrincipal User user){
        List<Event> derniersEvenements = eventRepository.findTop5ByOrderByDateEventDesc();
        List<News> derniersNews = newsRepository.findAllByOrderByDateNDesc(PageRequest.of(0, 6));

        model.addAttribute("derniersEvenements", derniersEvenements);
        model.addAttribute("news", derniersNews);
        model.addAttribute("user", user);

        return "index";
    }

}

