package fr.esic.mastering.services;


import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.VerdictDecision;
import fr.esic.mastering.repository.DecisionRepository;
import fr.esic.mastering.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecisionService {

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    /**
     * Ajouter une nouvelle décision basée sur la moyenne.
     *
     * @param candidatId L'ID du candidat
     * @param juryId     L'ID du jury (optionnel)
     * @param commentaireFinal Commentaire final
     * @return La décision ajoutée
     */
    public Decision addDecision(Long candidatId, Long juryId, String commentaireFinal) {
        // Récupérer les évaluations du candidat
        List<Evaluation> evaluations = evaluationRepository.findByCandidatId(candidatId);

        if (evaluations.isEmpty()) {
            throw new RuntimeException("Aucune évaluation trouvée pour le candidat ID : " + candidatId);
        }

        // Calculer la moyenne globale
        double moyenne = evaluations.stream()
                                    .mapToDouble(Evaluation::calculerMoyenne)
                                    .average()
                                    .orElse(0);

        // Déterminer le verdict
        VerdictDecision verdict;
        if (moyenne >= 10) {
            verdict = VerdictDecision.ADMIS;
        } else if (moyenne >= 9) {
            verdict = VerdictDecision.RATTRAPAGE;
        } else {
            verdict = VerdictDecision.NON_ADMIS;
        }
        Evaluation evaluation = evaluations.get(0);


        // Dans ton service
        Optional<Decision> existing = decisionRepository.findByEvaluation_Id(evaluation.getId());
        if (existing.isPresent()) {
            // Met à jour la décision existante
            Decision decision = existing.get();
            decision.setVerdict(verdict);
            decision.setCommentaireFinal(commentaireFinal);
            // ... autres champs à mettre à jour
            return decisionRepository.save(decision);
        } else {
            // Crée une nouvelle décision
            Decision decision = new Decision();
            decision.setCandidat(evaluation.getCandidat());
            decision.setJury(juryId != null ? evaluation.getJury() : null);
            decision.setEvaluation(evaluation);
            decision.setCommentaireFinal(commentaireFinal);
            decision.setVerdict(verdict);
            return decisionRepository.save(decision);
        }
    }

    /**
     * Récupérer toutes les décisions.
     */
    public List<Decision> getAllDecisions() {
        return decisionRepository.findAll();
    }

    /**
     * Récupérer les décisions d'un candidat.
     */
    public List<Decision> getDecisionsByCandidat(Long candidatId) {
        return decisionRepository.findByCandidatId(candidatId);
    }

    /**
     * Supprimer une décision par ID.
     */
    public void deleteDecision(Long id) {
        decisionRepository.deleteById(id);
    }
}
