package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.repositories.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/epreuves")
public class disciplineController {

    @Autowired
    private DisciplineRepository disciplineRepository;


    @GetMapping("")
    public String listDisciplines(Model model) {
        List<Discipline> disciplinesList = (List<Discipline>) disciplineRepository.findAll();
        model.addAttribute("disciplineList", disciplinesList);
        model.addAttribute("discipline", new Discipline()); // Pour le formulaire d'ajout
        return "epreuves"; // Afficher la vue epreuves.html
    }

    @PostMapping("add")
    public String addDiscipline(@ModelAttribute Discipline discipline) {
        disciplineRepository.save(discipline);
        return "redirect:/epreuves"; // Rediriger vers la vue epreuves.html
    }

    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Discipline> discipline = disciplineRepository.findById(id);
        if (discipline.isPresent()) {
            model.addAttribute("disciplineedit", discipline.get());
            return "epreuves"; // Afficher la vue epreuves.html (avec le formulaire d'édition)
        }
        return "redirect:/epreuves";
    }
    @PostMapping("edit/{id}")
    public String updateDiscipline(@PathVariable Long id, @ModelAttribute Discipline updatedDiscipline) {
        if (disciplineRepository.existsById(id)) {
            updatedDiscipline.setId(id);
            disciplineRepository.save(updatedDiscipline);
        }
        return "redirect:/epreuves";
    }
    @GetMapping("delete/{id}")
    public String deleteDiscipline(@PathVariable Long id) {
        disciplineRepository.deleteById(id);
        return "redirect:/epreuves";
    }
}
