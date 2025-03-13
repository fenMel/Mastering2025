package fr.esic.mastering.api;

import fr.esic.mastering.dto.SessionDeFormationDTO;
import fr.esic.mastering.dto.SessionDeFormationRequest;
import fr.esic.mastering.services.AnnonceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionDeFormationController {
    private final AnnonceService.SessionDeFormationService sessionService;

    @GetMapping
    public ResponseEntity<List<SessionDeFormationDTO>> getAllSessions() {
        log.info("Retrieving all sessions");
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDeFormationDTO> getSessionById(@PathVariable Long id) {
        log.info("Retrieving session with ID: {}", id);
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATEUR')")
    public ResponseEntity<SessionDeFormationDTO> createSession(
            @Valid @RequestBody SessionDeFormationRequest request
    ) {
        log.info("Creating new session: {}", request.getNom());
        SessionDeFormationDTO createdSession = sessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATEUR')")
    public ResponseEntity<SessionDeFormationDTO> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody SessionDeFormationRequest request
    ) {
        log.info("Updating session with ID: {}", id);
        return ResponseEntity.ok(sessionService.updateSession(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        log.info("Deleting session with ID: {}", id);
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<SessionDeFormationDTO>> getActiveSessions() {
        log.info("Retrieving all active sessions");
        return ResponseEntity.ok(sessionService.getActiveSessions());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<SessionDeFormationDTO>> getUpcomingSessions() {
        log.info("Retrieving all upcoming sessions");
        return ResponseEntity.ok(sessionService.getUpcomingSessions());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<SessionDeFormationDTO>> getCompletedSessions() {
        log.info("Retrieving all completed sessions");
        return ResponseEntity.ok(sessionService.getCompletedSessions());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SessionDeFormationDTO>> searchSessions(
            @RequestParam String query
    ) {
        log.info("Searching sessions with query: {}", query);
        return ResponseEntity.ok(sessionService.searchSessionsByName(query));
    }

    @PostMapping("/{sessionId}/soutenances/{soutenanceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATEUR')")
    public ResponseEntity<SessionDeFormationDTO> addSoutenanceToSession(
            @PathVariable Long sessionId,
            @PathVariable Long soutenanceId
    ) {
        log.info("Adding soutenance {} to session {}", soutenanceId, sessionId);
        return ResponseEntity.ok(sessionService.addSoutenanceToSession(sessionId, soutenanceId));
    }

    @DeleteMapping("/{sessionId}/soutenances/{soutenanceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATEUR')")
    public ResponseEntity<SessionDeFormationDTO> removeSoutenanceFromSession(
            @PathVariable Long sessionId,
            @PathVariable Long soutenanceId
    ) {
        log.info("Removing soutenance {} from session {}", soutenanceId, sessionId);
        return ResponseEntity.ok(sessionService.removeSoutenanceFromSession(sessionId, soutenanceId));
    }
}