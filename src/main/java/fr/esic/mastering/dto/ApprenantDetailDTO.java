package fr.esic.mastering.dto;

import fr.esic.mastering.entities.StatutInscription;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApprenantDetailDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private boolean emailVerified;
    private StatutInscription status;
    private List<InscriptionDTO> inscriptions;
    private List<DocumentDTO> documents;

}
