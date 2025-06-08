package fr.esic.mastering.api;

import fr.esic.mastering.dto.EvaluationDTO;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.services.EvaluationService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationRest {

    @Autowired
    private EvaluationService evaluationService;

    /**
     * Endpoint pour récupérer toutes les évaluations sous forme de DTO.
     *
     * @return Liste des évaluations avec les données et la moyenne calculée
     */
    @GetMapping
    public ResponseEntity<?> getAllEvaluations() {
        try {
            List<Evaluation> evaluations = evaluationService.getAll();

            // Convertir les entités Evaluation en DTO
            List<EvaluationDTO> dtos = evaluations.stream()
                    .map(evaluation -> evaluationService.convertToDTO(evaluation))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des évaluations.");
        }
    }

    /**
     * Endpoint pour ajouter une évaluation.
     *
     * @param evaluation L'évaluation à ajouter
     * @return Une réponse avec un message de succès ou d'échec
     */
    //////// 
    @PostMapping
    public ResponseEntity<?> addEvaluation(@RequestBody  @Valid Evaluation evaluation) {
        try {
            Evaluation savedEvaluation = evaluationService.addEvaluation(evaluation);
            return ResponseEntity.ok("Évaluation ajoutée avec succès, ID : " + savedEvaluation.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'ajout de l'évaluation.");
        }
    }
    /*
    @PostMapping
    public ResponseEntity<?> addEvaluation(@RequestBody  @Valid Evaluation evaluation) {
        try {
            Evaluation savedEvaluation = evaluationService.addEvaluation(evaluation);
            return ResponseEntity.ok("Évaluation ajoutée avec succès, ID : " + savedEvaluation.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'ajout de l'évaluation.");
        }
    }
*/
    /**
     * Endpoint pour récupérer les évaluations d'un candidat.
     *
     * @param candidatId L'ID du candidat
     * @return Liste des évaluations associées au candidat ou un message d'erreur
     */
    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<?> getEvaluationsByCandidat(@PathVariable Long candidatId) {
        try {
            List<Evaluation> evaluations = evaluationService.getEvaluationsByCandidat(candidatId);
            if (evaluations.isEmpty()) {
                return ResponseEntity.status(404).body("Aucune évaluation trouvée pour le candidat ID : " + candidatId);
            }
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des évaluations pour le candidat.");
        }
    }

    /**
     * Endpoint pour récupérer les évaluations d'un jury.
     *
     * @param juryId L'ID du jury
     * @return Liste des évaluations associées au jury ou un message d'erreur
     */
    @GetMapping("/jury/{juryId}")
    public ResponseEntity<?> getEvaluationsByJury(@PathVariable Long juryId) {
        try {
            List<Evaluation> evaluations = evaluationService.getEvaluationsByJury(juryId);
            if (evaluations.isEmpty()) {
                return ResponseEntity.status(404).body("Aucune évaluation trouvée pour le jury ID : " + juryId);
            }
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des évaluations pour le jury.");
        }
    }

    /**
     * Endpoint pour mettre à jour une évaluation.
     *
     * @param id            L'ID de l'évaluation à mettre à jour
     * @param newEvaluation Les nouvelles données de l'évaluation
     * @return Une réponse avec un message de succès ou d'échec
     */


    /**
     * Endpoint pour supprimer une évaluation.
     *
     * @param id L'ID de l'évaluation à supprimer
     * @return Une réponse avec un message de succès ou d'échec
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return ResponseEntity.noContent().build(); // 204 No Content, standard REST
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}/reset")
    // THIS ANNOTATION IS CRITICAL FOR METHOD-LEVEL SECURITY
    @PreAuthorize("hasAuthority('CORDINATEUR')")
    // @PreAuthorize("hasAnyRole('CORDINATEUR', 'JURY')")
    public ResponseEntity<Evaluation> resetEvaluation(@PathVariable Long id) {
        try {
            Evaluation resetEval = evaluationService.resetEvaluationData(id);
            return ResponseEntity.ok(resetEval);
        } catch (RuntimeException e) {
            // Log the exception for more details on the backend
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvaluationById(@PathVariable Long id) {
        try {
            Optional<Evaluation> evaluation = evaluationService.getById(id);
            if (evaluation.isPresent()) {
                return ResponseEntity.ok(evaluation.get());
            } else {
                return ResponseEntity.status(404).body("Évaluation non trouvée");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération de l'évaluation.");
        }
    }

    @PutMapping("/{id}/evaluer")
    public ResponseEntity<Void> evaluerEvaluation(@PathVariable Long id) {
        evaluationService.markAsEvaluated(id); // This will now resolve!
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation updatedEvaluation) {
        Evaluation result = evaluationService.updateEvaluation(id, updatedEvaluation); // This will now resolve!
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Endpoint pour récupérer les informations d'un jury par son ID
     * @param juryId L'ID du jury (qui est un User avec rôle JURY)
     * @return Les informations du jury
     */
    @GetMapping("/jury-info/{juryId}")
    public ResponseEntity<?> getJuryInfo(@PathVariable Long juryId) {
        try {
            // Utilisez le service User pour récupérer le jury
            User jury = evaluationService.getJuryInfo(juryId);
            if (jury == null) {
                return ResponseEntity.status(404).body("Jury non trouvé avec ID : " + juryId);
            }

            // Créez un DTO minimal si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("id", jury.getId());
            response.put("nom", jury.getNom());
            response.put("prenom", jury.getPrenom());
            response.put("email", jury.getEmail());
            // Ajoutez d'autres champs si nécessaire

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des informations du jury.");
        }
    }
}
