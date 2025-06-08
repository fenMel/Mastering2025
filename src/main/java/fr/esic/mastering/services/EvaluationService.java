package fr.esic.mastering.services;

import fr.esic.mastering.dto.EvaluationDTO;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.User; // Assuming User is your entity for both jury and candidat
import fr.esic.mastering.repository.EvaluationRepository;
import fr.esic.mastering.repository.UserRepository; // Assuming you have a UserRepository for User entities

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private UserRepository userRepository; // To fetch User entities for jury and candidat

    /**
     * Ajouter une nouvelle évaluation dans la base de données.
     * La moyenne est calculée automatiquement avant l'enregistrement.
     *
     * @param evaluation L'évaluation à ajouter
     * @return L'évaluation ajoutée avec son ID généré
     */
    @Transactional
    public Evaluation addEvaluation(Evaluation evaluation) {
        // Validate basic integrity of the evaluation object (e.g., notes range, comment)
        List<String> validationErrors = validateEvaluation(evaluation);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", validationErrors));
        }

        // Ensure jury and candidat are managed entities
        if (evaluation.getJury() == null || evaluation.getJury().getId() == null) {
            throw new IllegalArgumentException("L'évaluation doit être associée à un jury.");
        }
        User jury = userRepository.findById(evaluation.getJury().getId())
                .orElseThrow(() -> new IllegalArgumentException("Jury non trouvé avec l'ID : " + evaluation.getJury().getId()));
        evaluation.setJury(jury);

        if (evaluation.getCandidat() == null || evaluation.getCandidat().getId() == null) {
            throw new IllegalArgumentException("L'évaluation doit être associée à un candidat.");
        }
        User candidat = userRepository.findById(evaluation.getCandidat().getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidat non trouvé avec l'ID : " + evaluation.getCandidat().getId()));
        evaluation.setCandidat(candidat);

        // Calculate average before saving
        evaluation.calculerMoyenne();

        return evaluationRepository.save(evaluation);
    }

    /**
     * Mettre à jour une évaluation existante.
     * La moyenne est recalculée automatiquement avant l'enregistrement.
     *
     * @param id            L'ID de l'évaluation à mettre à jour
     * @param newEvaluation Les nouvelles données de l'évaluation
     * @return L'évaluation mise à jour
     */
    @Transactional
    public Evaluation updateEvaluation(Long id, Evaluation newEvaluation) {
        return evaluationRepository.findById(id).map(evaluation -> {
            // Update fields from the newEvaluation object
            evaluation.setNotePresentation(newEvaluation.getNotePresentation());
            evaluation.setNoteContenu(newEvaluation.getNoteContenu());
            evaluation.setNoteClarte(newEvaluation.getNoteClarte());
            evaluation.setNotePertinence(newEvaluation.getNotePertinence());
            evaluation.setNoteReponses(newEvaluation.getNoteReponses());
            evaluation.setCommentaire(newEvaluation.getCommentaire());

            // Validate after updating notes
            List<String> validationErrors = validateEvaluation(evaluation);
            if (!validationErrors.isEmpty()) {
                throw new IllegalArgumentException(String.join("; ", validationErrors));
            }

            // Recalculate average after update
            evaluation.calculerMoyenne();

            return evaluationRepository.save(evaluation);
        }).orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'ID : " + id));
    }

    /**
     * Marque une évaluation comme "évaluée" (or finalizes it).
     * This method ensures the average is calculated and the evaluation is saved.
     * Add any status update logic here if your Evaluation entity has a status field.
     *
     * @param id L'ID de l'évaluation à marquer comme évaluée
     */
    @Transactional
    public void markAsEvaluated(Long id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            // If you have a 'status' field (e.g., 'PENDING', 'COMPLETED'), set it here.
            // For example: evaluation.setStatus("COMPLETED");
            // If you have a 'date_evaluated' field, set it here.
            // evaluation.setDateEvaluated(LocalDateTime.now());

            evaluation.calculerMoyenne(); // Ensure average is re-calculated and saved
            evaluationRepository.save(evaluation);
        } else {
            throw new RuntimeException("Évaluation introuvable avec l'ID : " + id);
        }
    }

    /**
     * Récupérer toutes les évaluations.
     *
     * @return Liste de toutes les évaluations
     */
    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }

    /**
     * Récupérer les évaluations pour un candidat donné.
     *
     * @param candidatId L'ID du candidat
     * @return Liste des évaluations associées au candidat
     */
    public List<Evaluation> getEvaluationsByCandidat(Long candidatId) {
        // Assuming findByCandidatId is a method in your EvaluationRepository
        // that correctly fetches evaluations by candidat's ID
        return evaluationRepository.findByCandidatId(candidatId);
    }

    /**
     * Récupérer les évaluations faites par un jury donné.
     *
     * @param juryId L'ID du jury
     * @return Liste des évaluations associées au jury
     */
    public List<Evaluation> getEvaluationsByJury(Long juryId) {
        // Assuming findByJuryId is a method in your EvaluationRepository
        // that correctly fetches evaluations by jury's ID
        return evaluationRepository.findByJuryId(juryId);
    }

    /**
     * Supprimer une évaluation par ID.
     *
     * @param id L'ID de l'évaluation à supprimer
     */
    @Transactional
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }

    /**
     * Convertir une entité Evaluation en DTO.
     *
     * @param evaluation L'entité Evaluation
     * @return Un DTO contenant les données de l'évaluation
     */
    public EvaluationDTO convertToDTO(Evaluation evaluation) {
        // Ensure null checks for jury and candidat if they might be null during DTO conversion
        // (though @NotNull constraints should prevent this in persistent state)
        Long juryId = (evaluation.getJury() != null) ? evaluation.getJury().getId() : null;
        Long candidatId = (evaluation.getCandidat() != null) ? evaluation.getCandidat().getId() : null;

        // Populate other fields for EvaluationDTO as needed
        String candidatNom = "";
        String candidatPrenom = "";
        String sujet = ""; // Assuming sujet is on Candidat/User or somewhere else
        String dateSoutenance = ""; // Assuming dateSoutenance is on Candidat/User or somewhere else
        String salle = ""; // Assuming salle is on Candidat/User or somewhere else

        // You might need to fetch the Candidat/User details if they are needed in the DTO
        // and not directly available from the Evaluation entity's lazy loading or immediate fetch.
        // For example:
        /*
        if (candidatId != null) {
            userRepository.findById(candidatId).ifPresent(c -> {
                // Assuming your User entity has getNom(), getPrenom(), getSujet(), etc.
                // Or you might need a dedicated Candidat entity if it holds these specific fields.
                // For now, these are placeholders.
                // candidatNom = c.getNom();
                // candidatPrenom = c.getPrenom();
                // sujet = c.getSujet(); // If User has a 'sujet' field
            });
        }
        */

        return new EvaluationDTO(
                evaluation.getId(),
                juryId,
                candidatId,
                evaluation.getNotePresentation(),
                evaluation.getNoteContenu(),
                evaluation.getNoteClarte(),
                evaluation.getNotePertinence(),
                evaluation.getNoteReponses(),
                evaluation.getMoyenne(),
                evaluation.getCommentaire()
                // Add other fields from EvaluationDTO constructor if they exist and are needed
                // For example, if EvaluationDTO expects candidatNom, candidatPrenom, sujet, etc.
                // You'll need to pass them here after fetching.
        );
    }

    /**
     * Récupérer une évaluation par ID.
     *
     * @param id L'ID de l'évaluation à récupérer
     * @return L'évaluation si trouvée, sinon vide
     */
    public Optional<Evaluation> getById(Long id) {
        return evaluationRepository.findById(id);
    }

    /**
     * Valide les notes de l'évaluation et la présence du commentaire.
     * Assumes notes are 0-20.
     *
     * @param evaluation L'évaluation à valider.
     * @return Une liste de messages d'erreur. La liste est vide si aucune erreur.
     */
    public List<String> validateEvaluation(Evaluation evaluation) {
        List<String> errors = new ArrayList<>();

        // Validate notes are within 0-20 range
        if (evaluation.getNotePresentation() < 0 || evaluation.getNotePresentation() > 20) {
            errors.add("La note de présentation doit être entre 0 et 20.");
        }
        if (evaluation.getNoteContenu() < 0 || evaluation.getNoteContenu() > 20) {
            errors.add("La note de contenu doit être entre 0 et 20.");
        }
        if (evaluation.getNoteClarte() < 0 || evaluation.getNoteClarte() > 20) {
            errors.add("La note de clarté doit être entre 0 et 20.");
        }
        if (evaluation.getNotePertinence() < 0 || evaluation.getNotePertinence() > 20) {
            errors.add("La note de pertinence doit être entre 0 et 20.");
        }
        if (evaluation.getNoteReponses() < 0 || evaluation.getNoteReponses() > 20) {
            errors.add("La note de réponses doit être entre 0 et 20.");
        }

        // Validate comment presence (though @NotNull and @Size handle this at entity level,
        // it's good to have service-level validation for immediate feedback before DB constraints hit)
        if (evaluation.getCommentaire() == null || evaluation.getCommentaire().trim().isEmpty()) {
            errors.add("Le commentaire est obligatoire.");
        } else if (evaluation.getCommentaire().trim().length() < 5) {
            errors.add("Le commentaire doit contenir au moins 5 caractères.");
        }

        return errors;
    }

    // Placeholder for filtering evaluations if needed in the future
    public List<Evaluation> filterEvaluations(String dateRange, String statut, String candidat) {
        // Implement filtering logic here
        return evaluationRepository.findAll();
    }
    public User getJuryInfo(Long juryId) {
        return userRepository.findJuryById(juryId)
                .orElseThrow(() -> new EntityNotFoundException("Jury non trouvé"));
    }
    public Evaluation resetEvaluationData(Long id) {
        Optional<Evaluation> optEval = evaluationRepository.findById(id);
        if (optEval.isPresent()) {
            Evaluation eval = optEval.get();
            eval.setNoteClarte(0);
            eval.setNoteContenu(0);
            eval.setNotePertinence(0);
            eval.setNotePresentation(0);
            eval.setNoteReponses(0);
            eval.setCommentaire(null);
            eval.setMoyenne(0);
            // Ajoute d'autres champs à réinitialiser si besoin
            return evaluationRepository.save(eval);
        } else {
            throw new RuntimeException("Évaluation non trouvée");
        }
    }
}