package fr.esic.mastering.dto;

import fr.esic.mastering.entities.StatutInscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionDTO {
    private Long id;
    private Long soutenanceId;
    private String soutenanceSujet;
    private String emailEtudiant;
    private LocalDateTime creneauHoraire;
    private StatutInscription statut;
}
