package fr.esic.mastering.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import fr.esic.mastering.entities.User;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String lieuxDeNaissance;
    private String role;

    // Change the field type to String to match how you're using it
    private String dateNaissance;

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        return dto;
    }

    
}
