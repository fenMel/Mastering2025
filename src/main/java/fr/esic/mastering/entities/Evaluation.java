package fr.esic.mastering.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Le jury est obligatoire")
    private User jury;

    @ManyToOne
    @NotNull(message = "Le candidat est obligatoire")
    private User candidat;

    private String sujet;
    private LocalDateTime dateHeure;

    private String commentaire;

    private double notePresentation;
    private double noteContenu;
    private double noteClarte;
    private double notePertinence;
    private double noteReponses;
    private double moyenne;

    // Coefficients
    private int coefPresentation = 2;
    private int coefContenu = 2;
    private int coefClarte = 1;
    private int coefPertinence = 1;
    private int coefReponses = 1;

    // Constructeur personnalisé (sans coefficients modifiables)
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

    public double calculerMoyenne() {
        double totalCoef = coefPresentation + coefContenu + coefClarte + coefPertinence + coefReponses;

        if (totalCoef == 0) return 0;

        double somme = (notePresentation * coefPresentation) +
                (noteContenu * coefContenu) +
                (noteClarte * coefClarte) +
                (notePertinence * coefPertinence) +
                (noteReponses * coefReponses);

        this.moyenne = somme / totalCoef;
        return this.moyenne;
    }
}
