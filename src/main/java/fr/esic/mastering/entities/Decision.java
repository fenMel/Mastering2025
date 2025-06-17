package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant la décision finale prise à partir d’une évaluation.
 * Chaque décision est liée à une évaluation unique et contient un verdict ainsi qu’un commentaire final.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Decision {

    /**
     * Identifiant unique de la décision (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Candidat concerné par la décision.
     */
    @ManyToOne
    private User candidat;

    /**
     * Jury ayant pris la décision.
     * Ce champ est optionnel selon la logique métier (peut être nul si décision prise automatiquement).
     */
    @ManyToOne
    private User jury;

    /**
     * L’évaluation associée à cette décision.
     * Relation 1-1 obligatoire et unique.
     */
    @OneToOne
    @JoinColumn(name = "evaluation_id", nullable = false, unique = true)
    private Evaluation evaluation;

    /**
     * Commentaire final justifiant ou expliquant la décision.
     */
    private String commentaireFinal;

    /**
     * Verdict de la décision (ADMIS, RATTRAPAGE, NON_ADMIS...).
     * Sauvegardé en tant que chaîne de caractères dans la base.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerdictDecision verdict;

    /**
     * Permet d'accéder à l'ID de l’évaluation sans exposer toute l’entité.
     *
     * @return L’ID de l’évaluation si elle existe, sinon null
     */
    public Long getEvaluationId() {
        return evaluation != null ? evaluation.getId() : null;
    }
}
