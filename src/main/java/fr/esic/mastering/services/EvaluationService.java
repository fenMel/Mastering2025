package fr.esic.mastering.services;

import fr.esic.mastering.dto.EvaluationDTO;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.EvaluationRepository;
import fr.esic.mastering.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service gérant la logique métier liée aux évaluations.
 * Inclut les opérations de création, mise à jour, suppression, conversion en DTO et validation.
 */
@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Ajoute une nouvelle évaluation après validation et calcul de la moyenne.
     *
     * @param evaluation L'évaluation à ajouter
     * @return L'entité Evaluation enregistrée
     */
    @Transactional
    public Evaluation addEvaluation(Evaluation evaluation) {
        List<String> validationErrors = validateEvaluation(evaluation);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", validationErrors));
        }

        // Vérifie et associe le jury
        if (evaluation.getJury() == null || evaluation.getJury().getId() == null) {
            throw new IllegalArgumentException("L'évaluation doit être associée à un jury.");
        }
        User jury = userRepository.findById(evaluation.getJury().getId())
                .orElseThrow(() -> new IllegalArgumentException("Jury non trouvé avec l'ID : " + evaluation.getJury().getId()));
        evaluation.setJury(jury);

        // Vérifie et associe le candidat
        if (evaluation.getCandidat() == null || evaluation.getCandidat().getId() == null) {
            throw new IllegalArgumentException("L'évaluation doit être associée à un candidat.");
        }
        User candidat = userRepository.findById(evaluation.getCandidat().getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidat non trouvé avec l'ID : " + evaluation.getCandidat().getId()));
        evaluation.setCandidat(candidat);

        evaluation.calculerMoyenne();
        return evaluationRepository.save(evaluation);
    }

    /**
     * Met à jour une évaluation existante et recalcule sa moyenne.
     *
     * @param id ID de l'évaluation à mettre à jour
     * @param newEvaluation Les nouvelles données
     * @return L'évaluation mise à jour
     */
    @Transactional
    public Evaluation updateEvaluation(Long id, Evaluation newEvaluation) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluation.setNotePresentation(newEvaluation.getNotePresentation());
            evaluation.setNoteContenu(newEvaluation.getNoteContenu());
            evaluation.setNoteClarte(newEvaluation.getNoteClarte());
            evaluation.setNotePertinence(newEvaluation.getNotePertinence());
            evaluation.setNoteReponses(newEvaluation.getNoteReponses());
            evaluation.setCommentaire(newEvaluation.getCommentaire());
            evaluation.setSujet(newEvaluation.getSujet());
            evaluation.setDateHeure(newEvaluation.getDateHeure());

            List<String> validationErrors = validateEvaluation(evaluation);
            if (!validationErrors.isEmpty()) {
                throw new IllegalArgumentException(String.join("; ", validationErrors));
            }

            evaluation.calculerMoyenne();
            return evaluationRepository.save(evaluation);
        }).orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'ID : " + id));
    }

    /**
     * Marque une évaluation comme évaluée, avec recalcul éventuel de la moyenne.
     *
     * @param id ID de l'évaluation
     */
    @Transactional
    public void markAsEvaluated(Long id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        if (evaluationOptional.isPresent()) {
            Evaluation evaluation = evaluationOptional.get();
            evaluation.calculerMoyenne();
            evaluationRepository.save(evaluation);
        } else {
            throw new RuntimeException("Évaluation introuvable avec l'ID : " + id);
        }
    }

    /**
     * Récupère toutes les évaluations.
     *
     * @return Liste de toutes les évaluations
     */
    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }

    /**
     * Récupère toutes les évaluations d'un candidat.
     *
     * @param candidatId ID du candidat
     * @return Liste des évaluations du candidat
     */
    public List<Evaluation> getEvaluationsByCandidat(Long candidatId) {
        return evaluationRepository.findByCandidatId(candidatId);
    }

    /**
     * Récupère toutes les évaluations faites par un jury.
     *
     * @param juryId ID du jury
     * @return Liste des évaluations
     */
    public List<Evaluation> getEvaluationsByJury(Long juryId) {
        return evaluationRepository.findByJuryId(juryId);
    }

    /**
     * Supprime une évaluation par son identifiant.
     *
     * @param id ID de l'évaluation
     */
    @Transactional
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }

    /**
     * Convertit une entité Evaluation en objet DTO pour l'envoi vers le frontend.
     *
     * @param evaluation L'évaluation à convertir
     * @return Un DTO d'évaluation
     */
    public EvaluationDTO convertToDTO(Evaluation evaluation) {
        Long juryId = evaluation.getJury() != null ? evaluation.getJury().getId() : null;
        Long candidatId = evaluation.getCandidat() != null ? evaluation.getCandidat().getId() : null;

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
                evaluation.getCommentaire(),
                evaluation.getSujet(),
                evaluation.getDateHeure() != null ? evaluation.getDateHeure().toString() : null
        );
    }

    /**
     * Récupère une évaluation par son ID.
     *
     * @param id L'identifiant de l'évaluation
     * @return Option contenant l'évaluation ou vide
     */
    public Optional<Evaluation> getById(Long id) {
        return evaluationRepository.findById(id);
    }

    /**
     * Valide les notes et le commentaire d'une évaluation.
     *
     * @param evaluation L'évaluation à valider
     * @return Liste de messages d'erreurs, vide si aucune erreur
     */
    public List<String> validateEvaluation(Evaluation evaluation) {
        List<String> errors = new ArrayList<>();

        if (evaluation.getNotePresentation() < 0 || evaluation.getNotePresentation() > 20)
            errors.add("La note de présentation doit être entre 0 et 20.");

        if (evaluation.getNoteContenu() < 0 || evaluation.getNoteContenu() > 20)
            errors.add("La note de contenu doit être entre 0 et 20.");

        if (evaluation.getNoteClarte() < 0 || evaluation.getNoteClarte() > 20)
            errors.add("La note de clarté doit être entre 0 et 20.");

        if (evaluation.getNotePertinence() < 0 || evaluation.getNotePertinence() > 20)
            errors.add("La note de pertinence doit être entre 0 et 20.");

        if (evaluation.getNoteReponses() < 0 || evaluation.getNoteReponses() > 20)
            errors.add("La note de réponses doit être entre 0 et 20.");

        if (evaluation.getCommentaire() == null || evaluation.getCommentaire().trim().isEmpty())
            errors.add("Le commentaire est obligatoire.");
        else if (evaluation.getCommentaire().trim().length() < 5)
            errors.add("Le commentaire doit contenir au moins 5 caractères.");

        return errors;
    }

    /**
     * (Optionnel) Méthode de filtrage placeholder pour une recherche avancée.
     *
     * @param dateRange Période de recherche
     * @param statut Statut éventuel
     * @param candidat Nom ou ID du candidat
     * @return Liste des évaluations filtrées (logique à implémenter)
     */
    public List<Evaluation> filterEvaluations(String dateRange, String statut, String candidat) {
        return evaluationRepository.findAll(); // à implémenter
    }

    /**
     * Récupère les informations d'un jury à partir de son ID.
     *
     * @param juryId ID du jury
     * @return L'utilisateur ayant le rôle jury
     */
    public User getJuryInfo(Long juryId) {
        return userRepository.findJuryById(juryId)
                .orElseThrow(() -> new EntityNotFoundException("Jury non trouvé"));
    }

    /**
     * Réinitialise une évaluation (remise à zéro des notes, commentaires, etc.).
     *
     * @param id ID de l'évaluation à réinitialiser
     * @return L'évaluation réinitialisée
     */
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
            return evaluationRepository.save(eval);
        } else {
            throw new RuntimeException("Évaluation non trouvée");
        }
    }
}
