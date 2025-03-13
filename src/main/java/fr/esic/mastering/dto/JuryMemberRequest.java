package fr.esic.mastering.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class JuryMemberRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Nom is required")
    private String nom;

    @NotBlank(message = "Prénom is required")
    private String prenom;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}