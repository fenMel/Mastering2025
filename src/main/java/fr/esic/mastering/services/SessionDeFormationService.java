package fr.esic.mastering.services;

import fr.esic.mastering.dto.SessionDeFormationDTO;
import fr.esic.mastering.dto.SessionDeFormationRequest;

import java.util.List;
import java.util.Optional;

public interface SessionDeFormationService {
    // CRUD operations
    SessionDeFormationDTO createSession(SessionDeFormationRequest request);

    List<SessionDeFormationDTO> getAllSessions();

    Optional<SessionDeFormationDTO> getSessionById(Long id);

    SessionDeFormationDTO updateSession(Long id, SessionDeFormationRequest request);

    void deleteSession(Long id);

    // Business operations
    List<SessionDeFormationDTO> getActiveSessions();

    List<SessionDeFormationDTO> getUpcomingSessions();

    List<SessionDeFormationDTO> getCompletedSessions();

    List<SessionDeFormationDTO> searchSessionsByName(String query);

    SessionDeFormationDTO addSoutenanceToSession(Long sessionId, Long soutenanceId);

    SessionDeFormationDTO removeSoutenanceFromSession(Long sessionId, Long soutenanceId);
}