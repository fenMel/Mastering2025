package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de dépôt (repository) pour l'entité Evaluation.
 * Permet d'effectuer des opérations CRUD et des requêtes personnalisées sur la table des évaluations.
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    /**
     * Récupère toutes les évaluations associées à un candidat donné.
     *
     * @param candidatId L'identifiant du candidat
     * @return Liste des évaluations liées à ce candidat
     */
    List<Evaluation> findByCandidatId(Long candidatId);

    /**
     * Récupère toutes les évaluations réalisées par un jury donné.
     *
     * @param juryId L'identifiant du jury
     * @return Liste des évaluations effectuées par ce jury
     */
    List<Evaluation> findByJuryId(Long juryId);
}
