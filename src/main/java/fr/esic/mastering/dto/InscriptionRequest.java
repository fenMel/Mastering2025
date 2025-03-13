package fr.esic.mastering.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionRequest {
    @NotNull(message = "L'ID de la soutenance est requis")
    private Long soutenanceId;

    @NotBlank(message = "L'email de l'étudiant est requis")
    @Email(message = "L'email doit être valide")
    private String emailEtudiant;

    @NotNull(message = "Le créneau horaire est requis")
    private LocalDateTime creneauHoraire;
}