package fr.esic.mastering.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SessionSoutenanceDTO {

    private Long sessionFormationId;  // L'ID de la session de formation associée
    private LocalDate dateDebutSoutenance;  // La date de début de la soutenance
    private String responsable;  // Le responsable de la soutenance
    private String commentaireDateDebut;  // Commentaire sur la date de début

}
