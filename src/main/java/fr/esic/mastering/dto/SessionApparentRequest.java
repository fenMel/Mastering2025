package fr.esic.mastering.dto;

import fr.esic.mastering.entities.StatutInscription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionApparentRequest {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private StatutInscription statut;
}
