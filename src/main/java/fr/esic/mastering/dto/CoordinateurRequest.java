package fr.esic.mastering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordinateurRequest {
    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Nom is required")
    private String nom;

    @NotBlank(message = "Prenom is required")
    private String prenom;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
