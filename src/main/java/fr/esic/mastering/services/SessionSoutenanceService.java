package fr.esic.mastering.services;

import fr.esic.mastering.dto.SessionSoutenanceDTO;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.repository.SessionFormationRepository;
import fr.esic.mastering.repository.SessionSoutenanceRepository;
import fr.esic.mastering.repository.SessionSoutenanceUserRepository;
import fr.esic.mastering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionSoutenanceService {

    @Autowired
    private SessionSoutenanceRepository sessionSoutenanceRepository;

    @Autowired
    private SessionFormationRepository sessionFormationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionSoutenanceUserRepository sessionSoutenanceUserRepository;

    // Récupérer toutes les sessions de soutenance
    public List<SessionSoutenance> getAllSessions() {
        return sessionSoutenanceRepository.findAll();
    }

    // Créer une nouvelle session de soutenance

    public SessionSoutenance createSessionSoutenance(SessionSoutenanceDTO sessionSoutenanceDTO) {
        // 1. Récupérer la session de formation en utilisant l'ID reçu dans le DTO
        SessionFormation sessionFormation = sessionFormationRepository.findById(sessionSoutenanceDTO.getSessionFormationId())
                .orElseThrow(() -> new RuntimeException("Session de formation non trouvée"));

        // 2. Créer une nouvelle instance de SessionSoutenance et y associer la session de formation
        SessionSoutenance sessionSoutenance = new SessionSoutenance();
        sessionSoutenance.setSessionFormation(sessionFormation);  // Associer la session de formation
        sessionSoutenance.setDateDebutSoutenance(sessionSoutenanceDTO.getDateDebutSoutenance());  // Définir la date de début
        sessionSoutenance.setResponsable(sessionSoutenanceDTO.getResponsable());  // Définir le responsable
        sessionSoutenance.setCommentaireDateDebut(sessionSoutenanceDTO.getCommentaireDateDebut());  // Commentaire sur la date de début

        // 3. Sauvegarder la session de soutenance dans la base de données
        return sessionSoutenanceRepository.save(sessionSoutenance);
    }

    public SessionSoutenanceUser addUserToSession(Long sessionId, Long userId, String role) {
        // Récupérer la session de soutenance et l'utilisateur
        SessionSoutenance sessionSoutenance = sessionSoutenanceRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session de soutenance non trouvée"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Créer une nouvelle instance de SessionSoutenanceUser
        SessionSoutenanceUser sessionSoutenanceUser = new SessionSoutenanceUser();
        sessionSoutenanceUser.setSessionSoutenance(sessionSoutenance);
        sessionSoutenanceUser.setUser(user);
        sessionSoutenanceUser.setRole(RoleType.valueOf(role)); // Assure-toi que role est valide

        // Sauvegarder la nouvelle liaison dans la base de données
        return sessionSoutenanceUserRepository.save(sessionSoutenanceUser);
    }

//    public SessionSoutenance createSession(SessionSoutenance sessionSoutenance) {
//        return sessionSoutenanceRepository.save(sessionSoutenance);
//    }

    // Récupérer une session de soutenance par son ID
    public Optional<SessionSoutenance> getSessionById(Long id) {
        return sessionSoutenanceRepository.findById(id);
    }

    // Mettre à jour une session de soutenance
    public SessionSoutenance updateSession(Long id, SessionSoutenance sessionSoutenance) {
        if (sessionSoutenanceRepository.existsById(id)) {
            sessionSoutenance.setId(id);
            return sessionSoutenanceRepository.save(sessionSoutenance);
        } else {
            return null; // Ou lancer une exception personnalisée
        }
    }

    // Supprimer une session de soutenance par son ID
    public void deleteSession(Long id) {
        sessionSoutenanceRepository.deleteById(id);
    }
}
