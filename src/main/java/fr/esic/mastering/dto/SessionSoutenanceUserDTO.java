package fr.esic.mastering.dto;

import lombok.Data;

@Data
public class SessionSoutenanceUserDTO {

    private Long sessionId;   // ID de la session de soutenance
    private Long userId;      // ID de l'utilisateur à associer à la session
    private String role;      // Rôle de l'utilisateur dans la session (par exemple, "COORDINATEUR", "JURY", etc.)
}
