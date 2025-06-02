package fr.esic.mastering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO {
    // Getters and setters
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String roleName; // Just the name of the role


}
