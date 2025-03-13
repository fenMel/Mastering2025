package fr.esic.mastering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDeFormationDTO {
    private Long id;
    private String nom;
    private String information;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private List<SoutenanceDetailDTO> soutenances = new ArrayList<>();

    // Helper fields
    private String status; // "UPCOMING", "ACTIVE", "COMPLETED"
    private long durationInDays;
}