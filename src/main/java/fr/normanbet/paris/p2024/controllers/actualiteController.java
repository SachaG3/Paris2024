package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.News;
import fr.normanbet.paris.p2024.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
public class actualiteController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/actualite")
    public String actualite(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) String sort,
            Model model
    ) {
        List<News> newsList;

        if (search != null && !search.isEmpty()) {
            // Si le paramètre de recherche est fourni, filtrez les résultats
            newsList = newsRepository.findByTitleContainingIgnoreCase(search);
        } else {
            // Sinon, récupérez toutes les actualités
            newsList = newsRepository.findAll();
        }

        // Appliquer le tri si le paramètre de tri est fourni
        if (sort != null && sort.equals("asc")) {
            newsList.sort((a, b) -> a.getDateN().compareTo(b.getDateN()));
        } else if (sort != null && sort.equals("desc")) {
            newsList.sort((a, b) -> b.getDateN().compareTo(a.getDateN()));
        }

        model.addAttribute("newsList", newsList);
        model.addAttribute("search", search);
        model.addAttribute("sortAsc", "asc".equals(sort));
        model.addAttribute("sortDesc", "desc".equals(sort));

        return "/actualite";
    }


    @GetMapping("/actualite/{title}/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        News article = newsRepository.findById(id).orElse(null);

        if (article == null) {
            // Gérer le cas où l'article n'est pas trouvé
            return "redirect:/actualite";
        }

        model.addAttribute("article", article);
        return "/article";
    }
}
