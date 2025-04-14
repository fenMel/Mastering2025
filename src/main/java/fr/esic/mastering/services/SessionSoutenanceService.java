package fr.esic.mastering.services;

import fr.esic.mastering.entities.SessionSoutenance;
import fr.esic.mastering.repository.SessionSoutenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionSoutenanceService {

    @Autowired
    private SessionSoutenanceRepository sessionSoutenanceRepository;

    // Récupérer toutes les sessions de soutenance
    public List<SessionSoutenance> getAllSessions() {
        return sessionSoutenanceRepository.findAll();
    }

    // Créer une nouvelle session de soutenance
    public SessionSoutenance createSession(SessionSoutenance sessionSoutenance) {
        return sessionSoutenanceRepository.save(sessionSoutenance);
    }

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
