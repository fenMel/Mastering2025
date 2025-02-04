package fr.esic.mastering.api;

import fr.esic.mastering.entities.Formation;
import org.springframework.http.HttpStatus;
import fr.esic.mastering.repository.FormationRepository;
import fr.esic.mastering.services.FormationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/formations")
public class FormationRest {

    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private FormationService formationService;
   
    // 2. Obtenir toutes les formations
    @GetMapping
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    // 3. Obtenir une formation par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Long id) {
        Optional<Formation> formation = formationRepository.findById(id);
        return formation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Mettre à jour une formation existante
    @PutMapping("/{id}")
    public ResponseEntity<Formation> updateFormation(@PathVariable Long id, @RequestBody Formation updatedFormation) {
        return formationRepository.findById(id).map(existingFormation -> {
            existingFormation.setNom(updatedFormation.getNom());
            existingFormation.setNiveau(updatedFormation.getNiveau());
            existingFormation.setCodeRncp(updatedFormation.getCodeRncp());
            existingFormation.setDescription(updatedFormation.getDescription());
            existingFormation.setDuree(updatedFormation.getDuree());
            existingFormation.setPrerequis(updatedFormation.getPrerequis());
            existingFormation.setObjectifs(updatedFormation.getObjectifs());
            Formation savedFormation = formationRepository.save(existingFormation);
            return ResponseEntity.ok(savedFormation);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Formation createFormation1(@RequestBody Formation formation) {
        return formationService.createFormation(formation);
    } 
    
    // 5. Supprimer une formation par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFormation(@PathVariable Long id) {
        return formationRepository.findById(id).map(existingFormation -> {
            formationRepository.delete(existingFormation);
            return ResponseEntity.ok("Formation avec l'ID " + id + " a été supprimée avec succès.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Formation avec l'ID " + id + " n'a pas été trouvée."));
    }
}
