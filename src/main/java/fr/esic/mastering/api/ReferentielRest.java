package fr.esic.mastering.api;

import fr.esic.mastering.dto.ReferentielDTO;
import fr.esic.mastering.entities.Referentiels;
import fr.esic.mastering.services.ReferentielService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/referentiels")
@CrossOrigin("*")
public class ReferentielRest {  // Assurez-vous que le nom de la classe est unique et clair
    private final ReferentielService referentielService;

    public ReferentielRest(ReferentielService referentielService) {
        this.referentielService = referentielService;
    }

    // ✅ Récupérer tous les référentiels
    @GetMapping
    public List<Referentiels> getAllReferentiel() {
        return referentielService.getAllReferentiels();
    }

    // ✅ Récupérer un référentiel par ID
    @GetMapping("/{id}")
    public Referentiels getReferentielById(@PathVariable Long id) {
        return referentielService.getReferentielById(id);
    }

    // ✅ Créer un référentiel à partir d'une formation existante
    // Assurez-vous que le DTO est utilisé pour la création
    @PostMapping
    public Referentiels createReferentiel(@RequestBody ReferentielDTO referentielDTO) {
        return referentielService.createReferentiel(referentielDTO);
    }

    // ✅ Mettre à jour un référentiel
    @PutMapping("/{id}")
    public Referentiels updateReferentiels(@PathVariable Long id, @RequestBody ReferentielDTO referentielDTO) {
        return referentielService.updateReferentiel(id, referentielDTO);
    }

    // ✅ Supprimer un référentiel par ID
    @DeleteMapping("/{id}")
    public void deleteReferentiel(@PathVariable Long id) {
        referentielService.deleteReferentiel(id);
    }

    // ✅ Récupérer tous les référentiels d'une formation spécifique
    @GetMapping("/formation/{formationId}")
    public List<Referentiels> getReferentielsByFormation(@PathVariable Long formationId) {
        return referentielService.getReferentielsByFormationId(formationId);
    }

   
}
