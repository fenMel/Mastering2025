package fr.esic.mastering.repository;

import fr.esic.mastering.entities.SessionSoutenanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionSoutenanceUserRepository extends JpaRepository<SessionSoutenanceUser, Long> {
    // Trouver les utilisateurs par session de soutenance
    List<SessionSoutenanceUser> findBySessionSoutenanceId(Long sessionId);
    
    // Trouver les utilisateurs par rôle dans une session de soutenance
    // List<SessionSoutenanceUser> findByRole(RoleType role); // à revoir le roletype (pas reconnu)
    
    // Trouver les utilisateurs par ID
    List<SessionSoutenanceUser> findByUserId(Long userId);
}
