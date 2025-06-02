package fr.esic.mastering.services;

import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.repository.SessionFormationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SessionFormationService {

    private final SessionFormationRepository sessionFormationRepository;

    public SessionFormationService(SessionFormationRepository sessionFormationRepository) {
        this.sessionFormationRepository = sessionFormationRepository;
    }

    // ✅ Récupérer toutes les sessions
    public List<SessionFormation> getAllSessions() {
        return sessionFormationRepository.findAll();
    }

    // ✅ Récupérer une session par ID
    public SessionFormation getSessionById(Long id) {
        Optional<SessionFormation> session = sessionFormationRepository.findById(id);
        return session.orElseThrow(() -> new RuntimeException("Session non trouvée avec l'id : " + id));
    }
    // ✅ Récupérer une session par Titre
    public SessionFormation getSessionByTitre(String titre) {
        Optional<SessionFormation> session = sessionFormationRepository.findByTitre(titre);
        return session.orElseThrow(() -> new RuntimeException("Session non trouvée avec le titre : " + titre));
    }

    // ✅ Créer une session
    public SessionFormation createSessionFormation(SessionFormation sessionFormation) {
        return sessionFormationRepository.save(sessionFormation);
    }

    // ✅ Mettre à jour une session
    public SessionFormation updateSessionFormation(Long id, SessionFormation updatedSessionFormation) {
        SessionFormation sessionFormation = getSessionById(id);

        sessionFormation.setTitre(updatedSessionFormation.getTitre());
        sessionFormation.setDescription(updatedSessionFormation.getDescription());
        sessionFormation.setDateDebut(updatedSessionFormation.getDateDebut());
        sessionFormation.setDateFin(updatedSessionFormation.getDateFin());
        sessionFormation.setFormation(updatedSessionFormation.getFormation());
        return sessionFormationRepository.save(sessionFormation);
    }

    // ✅ Supprimer une session
   

    // ✅ Supprimer une session Forlation
    @Transactional
    public void deleteSessionFormation(Long id) {
        SessionFormation session = getSessionById(id);
        sessionFormationRepository.delete(session);
    }

    // ❓ Exemple : récupérer les sessions par un attribut personnalisé (si besoin)
    // public List<SessionFormation> getSessionsByFormationId(Long formationId) {
    //     return sessionFormationRepository.findByFormation_Id(formationId);
    // }
}
