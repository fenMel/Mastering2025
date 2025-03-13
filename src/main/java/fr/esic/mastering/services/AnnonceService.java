package fr.esic.mastering.services;

import fr.esic.mastering.entities.Annonce;
import fr.esic.mastering.entities.SessionEntrainement;
import fr.esic.mastering.repository.AnnonceRepository;
import fr.esic.mastering.repository.SessionEntrainementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepo;

    @Autowired
    private SessionEntrainementRepository sessionRepo;

    public Annonce publierAnnonce(Long sessionId, Annonce annonce) {
        SessionEntrainement session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session introuvable."));
        annonce.setSessionEntrainement(session);
        annonce.setDatePublication(LocalDateTime.now());
        return annonceRepo.save(annonce);
    }

    public List<Annonce> recupererAnnonces(Long sessionId) {
        return annonceRepo.findBySessionEntrainementId(sessionId);
    }

    public static interface SessionDeFormationService {
        // CRUD operations
        fr.esic.mastering.dto.SessionDeFormationDTO createSession(fr.esic.mastering.dto.SessionDeFormationRequest request);

        List<fr.esic.mastering.dto.SessionDeFormationDTO> getAllSessions();

        Optional<fr.esic.mastering.dto.SessionDeFormationDTO> getSessionById(Long id);

        fr.esic.mastering.dto.SessionDeFormationDTO updateSession(Long id, fr.esic.mastering.dto.SessionDeFormationRequest request);

        void deleteSession(Long id);

        // Business operations
        List<fr.esic.mastering.dto.SessionDeFormationDTO> getActiveSessions();

        List<fr.esic.mastering.dto.SessionDeFormationDTO> getUpcomingSessions();

        List<fr.esic.mastering.dto.SessionDeFormationDTO> getCompletedSessions();

        List<fr.esic.mastering.dto.SessionDeFormationDTO> searchSessionsByName(String query);

        fr.esic.mastering.dto.SessionDeFormationDTO addSoutenanceToSession(Long sessionId, Long soutenanceId);

        fr.esic.mastering.dto.SessionDeFormationDTO removeSoutenanceFromSession(Long sessionId, Long soutenanceId);
    }
}
