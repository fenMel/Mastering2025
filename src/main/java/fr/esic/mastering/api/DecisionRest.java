package fr.esic.mastering.api;

import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.services.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les décisions prises sur les évaluations des candidats.
 */
@RestController
@RequestMapping("/api/decisions")
public class DecisionRest {

    @Autowired
    private DecisionService decisionService;

    /**
     * Ajoute une décision basée sur la moyenne calculée des évaluations d’un candidat.
     * La décision est prise par un jury avec un commentaire final.
     *
     * @param requestData Contient l’ID du candidat, l’ID du jury (optionnel) et un commentaire final
     * @return Réponse contenant un message de succès et l’ID de la décision, ou une erreur
     */
    @PostMapping
    public ResponseEntity<?> addDecision(@RequestBody Map<String, Object> requestData) {
        try {
            Long candidatId = Long.valueOf(requestData.get("candidatId").toString());
            Long juryId = requestData.get("juryId") != null ? Long.valueOf(requestData.get("juryId").toString()) : null;
            String commentaireFinal = requestData.get("commentaireFinal").toString();

            Decision decision = decisionService.addDecision(candidatId, juryId, commentaireFinal);

            // Construction de la réponse JSON
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Décision ajoutée avec succès");
            response.put("id", decision.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Récupère toutes les décisions existantes dans le système.
     *
     * @return Liste complète des décisions ou message d'erreur
     */
    @GetMapping
    public ResponseEntity<?> getAllDecisions() {
        try {
            List<Decision> decisions = decisionService.getAllDecisions();
            return ResponseEntity.ok(decisions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des décisions.");
        }
    }

    /**
     * Récupère toutes les décisions liées à un candidat spécifique.
     *
     * @param candidatId ID du candidat
     * @return Liste des décisions du candidat ou message si aucune trouvée
     */
    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<?> getDecisionsByCandidat(@PathVariable Long candidatId) {
        try {
            List<Decision> decisions = decisionService.getDecisionsByCandidat(candidatId);
            if (decisions.isEmpty()) {
                return ResponseEntity.status(404).body("Aucune décision trouvée pour le candidat ID : " + candidatId);
            }
            return ResponseEntity.ok(decisions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des décisions pour le candidat.");
        }
    }

    /**
     * Supprime une décision par son identifiant.
     *
     * @param id ID de la décision à supprimer
     * @return Message de succès ou message d'erreur en cas d’échec
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecision(
            @PathVariable Long id,
            @RequestHeader(value = "X-User", required = false) String archivePar) {
        decisionService.deleteDecision(id, archivePar);
        return ResponseEntity.noContent().build();
    }
}
