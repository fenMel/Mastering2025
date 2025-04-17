package fr.esic.mastering.dto;

import java.time.LocalDate;

import lombok.Data;
import java.util.List;

@Data
public class SessionFormationDTO {

    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long formationId;
    private List<Long> CandidatsIds; // Liste des IDs d'étudiants inscrits
}

