package fr.esic.mastering.dto;

import fr.esic.mastering.entities.Decision;

public class DecisionDTO {
    private Long id;
    private String verdict;
    private String commentaire_final;
    private Double moyenne;

    // Ajoute d'autres champs si besoin (candidat, jury...)

    public DecisionDTO(Decision decision) {
        this.id = decision.getId();
        this.verdict = decision.getVerdict() != null ? decision.getVerdict().name() : null;
        this.commentaire_final = decision.getCommentaireFinal();
        this.moyenne = decision.getEvaluation() != null ? decision.getEvaluation().getMoyenne() : null;
    }

}