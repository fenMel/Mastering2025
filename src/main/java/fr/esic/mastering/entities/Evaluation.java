package fr.esic.mastering.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant une évaluation d’un candidat par un jury.
 * Contient les différentes notes, un commentaire, la date, le sujet et la moyenne calculée.
 */
@NoArgsConstructor
@Data
@Entity
public class Evaluation {

    /**
     * Identifiant unique de l’évaluation (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le membre du jury ayant effectué l’évaluation.
     */
    @ManyToOne
    @NotNull(message = "Le jury est obligatoire")
    private User jury;

    /**
     * Le candidat évalué.
     */
    @ManyToOne
    @NotNull(message = "Le candidat est obligatoire")
    private User candidat;

    /**
     * Sujet de la soutenance ou de l'évaluation.
     */
    private String sujet;

    /**
     * Date et heure de la soutenance ou de l'évaluation.
     */
    private LocalDateTime dateHeure;

    /**
     * Commentaire global de l’évaluateur.
     */
    private String commentaire;

    // Notes sur 20
    private double notePresentation;
    private double noteContenu;
    private double noteClarte;
    private double notePertinence;
    private double noteReponses;

    /**
     * Moyenne pondérée calculée à partir des notes et coefficients.
     */
    private double moyenne;

    // Coefficients appliqués à chaque critère (modifiable si besoin)
    private int coefPresentation = 2;
    private int coefContenu = 2;
    private int coefClarte = 1;
    private int coefPertinence = 1;
    private int coefReponses = 1;

    /**
     * Constructeur personnalisé utilisé pour créer une évaluation avec toutes ses données,
     * sans modification des coefficients par défaut.
     *
     * @param id Identifiant de l’évaluation
     * @param jury Membre du jury
     * @param candidat Candidat évalué
     * @param sujet Sujet de l’évaluation
     * @param dateHeure Date et heure de l’évaluation
     * @param commentaire Commentaire de l’évaluateur
     * @param notePresentation Note pour la présentation
     * @param noteContenu Note pour le contenu
     * @param noteClarte Note pour la clarté
     * @param notePertinence Note pour la pertinence
     * @param noteReponses Note pour la réponse aux questions
     * @param moyenne Moyenne déjà calculée (facultative à l'appel)
     */
    public Evaluation(Long id, User jury, User candidat, String sujet, LocalDateTime dateHeure, String commentaire,
                      double notePresentation, double noteContenu, double noteClarte,
                      double notePertinence, double noteReponses, double moyenne) {
        this.id = id;
        this.jury = jury;
        this.candidat = candidat;
        this.sujet = sujet;
        this.dateHeure = dateHeure;
        this.commentaire = commentaire;
        this.notePresentation = notePresentation;
        this.noteContenu = noteContenu;
        this.noteClarte = noteClarte;
        this.notePertinence = notePertinence;
        this.noteReponses = noteReponses;
        this.moyenne = moyenne;
    }

    /**
     * Calcule et met à jour la moyenne pondérée de l’évaluation selon les coefficients.
     *
     * @return La moyenne arrondie à deux décimales
     */
    public double calculerMoyenne() {
        double totalCoef = coefPresentation + coefContenu + coefClarte + coefPertinence + coefReponses;

        if (totalCoef == 0) return 0;

        double somme = (notePresentation * coefPresentation) +
                (noteContenu * coefContenu) +
                (noteClarte * coefClarte) +
                (notePertinence * coefPertinence) +
                (noteReponses * coefReponses);

        this.moyenne = Math.round((somme / totalCoef) * 100.0) / 100.0;
        return this.moyenne;
    }
}
