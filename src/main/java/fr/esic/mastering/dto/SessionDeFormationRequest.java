package fr.esic.mastering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDeFormationRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Les informations sont obligatoires")
    private String information;


    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;  // Changed from LocalDateTime

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private List<Long> soutenanceIds;
}
