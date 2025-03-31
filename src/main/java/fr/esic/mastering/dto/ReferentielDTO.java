package fr.esic.mastering.dto;

import lombok.Data;

@Data
public class ReferentielDTO {
    private String nom;
    private String description;
    private String objectifs;
    private String criteres;
    private Long formationId;
}
