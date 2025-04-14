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
    public SessionFormation createSession(SessionFormation session) {
        return sessionFormationRepository.save(session);
    }

    // ✅ Mettre à jour une session
    public SessionFormation updateSession(Long id, SessionFormation updatedSession) {
        SessionFormation session = getSessionById(id);

        session.setTitre(updatedSession.getTitre());
        session.setDescription(updatedSession.getDescription());
        session.setDateDebut(updatedSession.getDateDebut());
        session.setDateFin(updatedSession.getDateFin());
        session.setFormation(updatedSession.getFormation());
        return sessionFormationRepository.save(session);
    }

    // ✅ Supprimer une session
    @Transactional
    public void deleteSession(Long id) {
        SessionFormation session = getSessionById(id);
        sessionFormationRepository.delete(session);
    }

    // ❓ Exemple : récupérer les sessions par un attribut personnalisé (si besoin)
    // public List<SessionFormation> getSessionsByFormationId(Long formationId) {
    //     return sessionFormationRepository.findByFormation_Id(formationId);
    // }
}
