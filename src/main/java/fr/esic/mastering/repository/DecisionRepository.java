package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.VerdictDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de dépôt (repository) pour l'entité Decision.
 * Permet de gérer les opérations CRUD et les requêtes personnalisées liées aux décisions rendues par le jury.
 */
public interface DecisionRepository extends JpaRepository<Decision, Long> {

    /**
     * Récupère toutes les décisions associées à un candidat spécifique.
     *
     * @param candidatId L'identifiant du candidat
     * @return Liste des décisions du candidat
     */
    List<Decision> findByCandidatId(Long candidatId);

    /**
     * Récupère toutes les décisions prises par un jury spécifique.
     *
     * @param juryId L'identifiant du jury
     * @return Liste des décisions prises par ce jury
     */
    List<Decision> findByJuryId(Long juryId);

    /**
     * Récupère toutes les décisions correspondant à un verdict donné.
     *
     * @param verdict Le type de verdict (ADMIS, RATTRAPAGE, NON_ADMIS, etc.)
     * @return Liste des décisions correspondant à ce verdict
     */
    List<Decision> findByVerdict(VerdictDecision verdict);

    /**
     * Recherche une décision associée à une évaluation donnée.
     * Utile pour éviter la duplication de décisions.
     *
     * @param evaluationId L'identifiant de l'évaluation
     * @return Une décision liée à cette évaluation, si elle existe
     */
    Optional<Decision> findByEvaluation_Id(Long evaluationId);
}
