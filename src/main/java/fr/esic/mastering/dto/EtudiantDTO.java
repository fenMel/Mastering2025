package fr.esic.mastering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String matricule; // Student ID number
    // Add other student-specific fields
}
