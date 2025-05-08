package fr.esic.mastering.mapper;

import fr.esic.mastering.dto.UserDTO;
import fr.esic.mastering.entities.User;

import java.time.LocalDate;
import java.time.ZoneId;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setTel(user.getTel());
        dto.setEmail(user.getEmail());
        dto.setLieuxDeNaissance(user.getLieuxDeNaissance());
        dto.setRole(user.getRole().getRoleUtilisateur().toString());

        if (user.getDateNaissance() != null) {
            LocalDate localDate = user.getDateNaissance()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setDateNaissance(localDate.toString());
        }

        return dto;
    }
}