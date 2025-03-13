package fr.esic.mastering.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EtudiantWithStatusDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String status;
}