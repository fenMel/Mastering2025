package fr.esic.mastering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoutenanceDetailDTO {
    private Long id;
    private String sujet;
    private String lieu;
    private LocalDateTime dateHeure;
    private List<ApprenantDetailDTO> etudiants;
    private List<UserDTO> juryMembers;
    private UserDTO coordinateur;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, etc.
}