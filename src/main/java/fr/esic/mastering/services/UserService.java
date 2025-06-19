package fr.esic.mastering.services;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import fr.esic.mastering.dto.UserDTO;

import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.entities.User;

import fr.esic.mastering.mapper.UserMapper;
import fr.esic.mastering.repository.SessionFormationRepository;
import fr.esic.mastering.repository.UserRepository;

@Service

public class UserService {

    private final UserRepository userRepository;
    private final SessionFormationRepository sessionFormationRepository;

    @Autowired

    public UserService(UserRepository userRepository, SessionFormationRepository sessionFormationRepository) {

        this.userRepository = userRepository;
        this.sessionFormationRepository = sessionFormationRepository;

    }

    // Obtenir tous les utilisateurs par rôle

    public List<UserDTO> getUsersByRole(String role) {

        RoleType roleType;

        try {

            roleType = RoleType.valueOf(role.toUpperCase());

        } catch (IllegalArgumentException e) {

            return List.of();

        }

        List<User> candidats = userRepository.findByRoleRoleUtilisateur(RoleType.CANDIDAT);

        return candidats.stream()

                .map(UserMapper::toDTO)

                .collect(Collectors.toList());

    }

    // Obtenir tous les candidats (RoleType = CANDIDAT)

    

    // Méthode utile si tu veux chercher des candidats par IDs

    public List<User> getCandidatsByIds(List<Long> ids) {

        return userRepository.findCandidatsByRoleAndIds(RoleType.CANDIDAT, ids);

    }

    public List<SessionFormation> getAllSessions() {
   return sessionFormationRepository.findAllWithFormationAndCandidats();
}



public List<UserDTO> getAllCandidats() {
    return userRepository.findByRole_RoleUtilisateur(RoleType.CANDIDAT).stream()
        .map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setNom(user.getNom());
            dto.setPrenom(user.getPrenom());
            dto.setEmail(user.getEmail());
            return dto;
        })
        .collect(Collectors.toList());
}


}
 