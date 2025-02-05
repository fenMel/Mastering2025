package fr.esic.mastering.api;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.services.FormationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formations")
@CrossOrigin("*")
public class FormationRest {
    private final FormationService formationService;

    public FormationRest(FormationService formationService) {
        this.formationService = formationService;
    }

    @GetMapping
    public List<Formation> getAllFormations() {
        return formationService.getAllFormations();
    }

    @PostMapping
    public Formation createFormation(@RequestBody Formation formation) {
        return formationService.createFormation(formation);
    }
}
