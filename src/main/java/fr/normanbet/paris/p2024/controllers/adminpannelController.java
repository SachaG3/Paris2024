package fr.normanbet.paris.p2024.controllers;

import fr.normanbet.paris.p2024.models.Athlete;
import fr.normanbet.paris.p2024.models.Discipline;
import fr.normanbet.paris.p2024.models.Sport;
import fr.normanbet.paris.p2024.models.User;
import fr.normanbet.paris.p2024.models.types.DisciplineType;
import fr.normanbet.paris.p2024.models.types.GenreType;
import fr.normanbet.paris.p2024.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion")
public class adminpannelController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;


    @GetMapping("/adminpanel")
    public String adminUserPanel(Model model) {
        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);
        return "/Gestion/adminpanel";
    }


    @GetMapping("/informationuser/{id}")
    public String showUserInfo(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "/Gestion/informationuser";
    }

    @GetMapping("/deactivate/{id}")
    public String showDeactivateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "/Gestion/deactivate";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateAccount(@PathVariable Long id, String password) {
        User user = userRepository.findById(id).orElse(null);


        if (user != null && user.getRole().getName().equals("admin") && passwordEncoder.matches(password, user.getPassword())) {
            user.setActive(false);
            userRepository.save(user);
        } else if (user != null && (user.getRole().getName().equals("member") || user.getRole().getName().equals("rédacteur"))) {
            user.setActive(false);
            userRepository.save(user);
        }

        return "redirect:/gestion/adminpanel";
    }


    @PostMapping("/activate/{id}")
    public String activateAccount(@PathVariable Long id, String password) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            if (user.getRole().getName().equals("admin") && passwordEncoder.matches(password, user.getPassword())) {
                user.setActive(true);
                userRepository.save(user);
            } else if (user.getRole().getName().equals("member") || user.getRole().getName().equals("rédacteur")) {
                user.setActive(true);
                userRepository.save(user);
            }
        }

        return "redirect:/gestion/adminpanel";
    }
    @GetMapping("/athlete/recherche")
    public String rechercheAthlete(@RequestParam("nomAthlete") String text, Model model) {
        text = "%" + text + "%";
        List<Athlete> athleteList = athleteRepository.findByFirstnameLikeOrLastnameLikeAllIgnoreCase(text, text);
        model.addAttribute("athleteList", athleteList);
        return "/Gestion/athlete";
    }

    @GetMapping("/athlete")
    public String actualite(Model model) {
        List<Athlete> athleteList = (List<Athlete>) athleteRepository.findAll();
        model.addAttribute("athleteList", athleteList);
        return "/Gestion/athlete";
    }

    @PostMapping("/athlete/update/{id}")
    public String updateAthlete(@PathVariable Long id,
                                @RequestParam String firstname,
                                @RequestParam String lastname,
                                @RequestParam String birthdate,
                                @RequestParam String genre,
                                Model model) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete not found"));

        athlete.setFirstname(firstname);
        athlete.setLastname(lastname);
        athlete.setBirthdate(LocalDate.parse(birthdate));
        athlete.setGenre(GenreType.valueOf(genre));

        athleteRepository.save(athlete);
        model.addAttribute("successMessage", "Athlete mis à jour avec succès !");
        return "/Gestion/athlete";
    }

    @DeleteMapping("/athlete/delete/{id}")
    public void deleteAthlete(@PathVariable Long id) {
        athleteRepository.deleteById(id);
    }

    @GetMapping("/athlete/modifierunathlete/{id}")
    public String modifierAthlete(@PathVariable Long id, Model model) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete not found"));
        model.addAttribute("athlete", athlete);
        return "/Gestion/modifierunathlete";
    }
    @GetMapping("/recherche")
    public String rechercheSports(@RequestParam("nomSport") String text, Model model) {
        List<Sport> sportList = sportRepository.findByNameIgnoreCaseStartingWith(text);
        model.addAttribute("sports", sportList);
        return "/gestion/sports";
    }



    @GetMapping("/sports")
    public String listSports(Model model) {
        List<Sport> sports = (List<Sport>) sportRepository.findAll();
        model.addAttribute("sports", sports);
        return "/gestion/sports";
    }

    @GetMapping("/ajouterunsport")
    public String showCreateSportForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "/gestion/ajouterunsport";
    }

    @PostMapping("/ajouterunsport")
    public String createSport(@ModelAttribute Sport sport,
                              @RequestParam(name = "discipline", required = false) String discipline,
                              RedirectAttributes redirectAttributes) {
        try {
            String currentUser = getCurrentUser();
            sport.setAddedBy(currentUser);
            sportRepository.save(sport);

            // Si des disciplines sont fournies, les enregistrer
            if (discipline != null && !discipline.isEmpty()) {
                String[] disciplineArray = discipline.split(",");
                for (String disciplineName : disciplineArray) {
                    Discipline newDiscipline = new Discipline();
                    newDiscipline.setName(disciplineName.trim());
                    newDiscipline.setType(DisciplineType.MIXTE);
                    newDiscipline.setSport(sport);
                    disciplineRepository.save(newDiscipline);
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Sport créé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du sport.");
        }

        return "redirect:/gestion/sports";
    }


    @GetMapping("/update/{id}")
    public String showUpdateSportForm(@PathVariable Long id, Model model) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        model.addAttribute("sport", sport);
        return "/gestion/sport";
    }

    @PostMapping("/update/{id}")
    public String updateSport(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam boolean individual,
                              @RequestParam boolean collective,
                              @RequestParam int sizeTeam) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        sport.setName(name);
        sport.setDescription(description);
        sport.setIndividual(individual);
        sport.setCollective(collective);
        sport.setSizeTeam(sizeTeam);

        sportRepository.save(sport);
        return "redirect:/gestion/sports";
    }
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    @GetMapping("/epreuves")
    public String listDisciplines(Model model) {
        List<Discipline> disciplinesList = (List<Discipline>) disciplineRepository.findAll();
        model.addAttribute("disciplineList", disciplinesList);
        model.addAttribute("discipline", new Discipline()); // Pour le formulaire d'ajout
        return "/Gestion/epreuves"; // Afficher la vue epreuves.html
    }

    @PostMapping("/epreuves/add")
    public String addDiscipline(@ModelAttribute Discipline discipline) {
        disciplineRepository.save(discipline);
        return "redirect:/gestion/epreuves"; // Rediriger vers la vue epreuves.html
    }

    @GetMapping("/epreuves/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Discipline> discipline = disciplineRepository.findById(id);
        if (discipline.isPresent()) {
            model.addAttribute("disciplineedit", discipline.get());
            return "/gestion/epreuves"; // Afficher la vue epreuves.html (avec le formulaire d'édition)
        }
        return "redirect:/gestion/epreuves";
    }
    @PostMapping("/epreuves/edit/{id}")
    public String updateDiscipline(@PathVariable Long id, @ModelAttribute Discipline updatedDiscipline) {
        if (disciplineRepository.existsById(id)) {
            updatedDiscipline.setId(id);
            disciplineRepository.save(updatedDiscipline);
        }
        return "redirect:/gestion/epreuves";
    }
    @GetMapping("/epreuves/delete/{id}")
    public String deleteDiscipline(@PathVariable Long id) {
        disciplineRepository.deleteById(id);
        return "redirect:/gestion/epreuves";
    }


}
