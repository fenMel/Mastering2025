package fr.esic.mastering.api;

import fr.esic.mastering.entities.Formation;
import org.springframework.http.HttpStatus;
import fr.esic.mastering.repository.FormationRepository;
import fr.esic.mastering.repository.SessionFormationRepository;
import fr.esic.mastering.services.FormationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/formations")
public class FormationRest {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private SessionFormationRepository sessionFormationRepository;

    @Autowired
    private FormationService formationService;
   
    // 2. Obtenir toutes les formations
    @GetMapping("/all")
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

    @PostMapping("/create")
    public ResponseEntity<Formation> createFormation(@RequestBody Formation newFormation) {
        Formation savedFormation = formationRepository.save(newFormation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFormation);
    }

    // 5. Supprimer une formation par son ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteFormation(@PathVariable Long id) {
        return formationRepository.findById(id).map(existingFormation -> {
            // Supprimer les sessions associées à la formation
            sessionFormationRepository.deleteByFormationId(id);

            // Supprimer la formation
            formationRepository.delete(existingFormation);
            return ResponseEntity.ok("Formation avec l'ID " + id + " a été supprimée avec succès.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Formation avec l'ID " + id + " n'a pas été trouvée."));
    }

  @PreAuthorize("hasAuthority('CORDINATEUR') or hasAuthority('SUPERVISOR') or hasAuthority('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<String> forceDeleteFormation(@PathVariable Long id) {
    Optional<Formation> formationOptional = formationRepository.findById(id);

    if (formationOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Formation avec l'ID " + id + " introuvable.");
    }

    Formation formation = formationOptional.get();

    sessionFormationRepository.deleteByFormation(formation);
    formationRepository.delete(formation);

    return ResponseEntity.ok("Formation supprimée avec toutes ses sessions.");
}

}
