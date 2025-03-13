package fr.esic.mastering.services.impl;

import fr.esic.mastering.dto.SessionDeFormationDTO;
import fr.esic.mastering.dto.SessionDeFormationRequest;
import fr.esic.mastering.dto.SoutenanceDetailDTO;
import fr.esic.mastering.entities.SessionDeFormation;
import fr.esic.mastering.entities.Soutenance;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.repository.SessionDeFormationRepository;
import fr.esic.mastering.repository.SoutenanceRepository;
import fr.esic.mastering.services.AnnonceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionDeFormationServiceImpl implements AnnonceService.SessionDeFormationService {
    private final SessionDeFormationRepository sessionRepository;
    private final SoutenanceRepository soutenanceRepository;

    @Override
    @Transactional
    public SessionDeFormationDTO createSession(SessionDeFormationRequest request) {
        log.info("Creating new session: {}", request.getNom());

        // Validate date range
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        // Create session entity
        LocalDateTime dateDebut = request.getDateDebut().atStartOfDay();
        LocalDateTime dateFin = request.getDateFin().atTime(23, 59, 59);

        SessionDeFormation session = SessionDeFormation.builder()
                .nom(request.getNom())
                .information(request.getInformation())
                .dateDebut(dateDebut)  // Using converted time
                .dateFin(dateFin)      // Using converted time
                .soutenance(new ArrayList<>())
                .build();
        // Add soutenances if requested
        if (request.getSoutenanceIds() != null && !request.getSoutenanceIds().isEmpty()) {
            List<Soutenance> soutenances = soutenanceRepository.findAllById(request.getSoutenanceIds());

            if (soutenances.size() != request.getSoutenanceIds().size()) {
                throw new ResourceNotFoundException("One or more soutenances not found");
            }

            // Set bidirectional relationship
            for (Soutenance soutenance : soutenances) {
                soutenance.setSessionDeFormation(session);
                session.getSoutenance().add(soutenance);
            }
        }

        // Save entity
        SessionDeFormation savedSession = sessionRepository.save(session);
        log.info("Created session with ID: {}", savedSession.getId());

        // Convert to DTO and return
        return convertToDTO(savedSession);
    }

    @Override
    public List<SessionDeFormationDTO> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SessionDeFormationDTO> getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public SessionDeFormationDTO updateSession(Long id, SessionDeFormationRequest request) {
        SessionDeFormation session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));

        // Validate date range
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        LocalDateTime dateDebut = request.getDateDebut().atStartOfDay();
        LocalDateTime dateFin = request.getDateFin().atTime(23, 59, 59);

        // Update fields
        session.setNom(request.getNom());
        session.setInformation(request.getInformation());
        session.setDateDebut(dateDebut);
        session.setDateFin(dateFin);

        // Handle soutenances update if provided
        if (request.getSoutenanceIds() != null) {
            // First remove all existing associations
            for (Soutenance soutenance : new ArrayList<>(session.getSoutenance())) {
                soutenance.setSessionDeFormation(null);
            }
            session.getSoutenance().clear();

            // Then add the new ones
            if (!request.getSoutenanceIds().isEmpty()) {
                List<Soutenance> soutenances = soutenanceRepository.findAllById(request.getSoutenanceIds());

                if (soutenances.size() != request.getSoutenanceIds().size()) {
                    throw new ResourceNotFoundException("One or more soutenances not found");
                }

                for (Soutenance soutenance : soutenances) {
                    soutenance.setSessionDeFormation(session);
                    session.getSoutenance().add(soutenance);
                }
            }
        }

        // Save and return
        return convertToDTO(sessionRepository.save(session));
    }

    @Override
    @Transactional
    public void deleteSession(Long id) {
        SessionDeFormation session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));

        // Remove bidirectional relationships
        for (Soutenance soutenance : new ArrayList<>(session.getSoutenance())) {
            soutenance.setSessionDeFormation(null);
            soutenanceRepository.save(soutenance);
        }

        sessionRepository.delete(session);
        log.info("Deleted session with ID: {}", id);
    }

    @Override
    public List<SessionDeFormationDTO> getActiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findByDateDebutBeforeAndDateFinAfter(now, now).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDeFormationDTO> getUpcomingSessions() {
        return sessionRepository.findByDateDebutAfterOrderByDateDebutAsc(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDeFormationDTO> getCompletedSessions() {
        return sessionRepository.findByDateFinBeforeOrderByDateFinDesc(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDeFormationDTO> searchSessionsByName(String query) {
        return sessionRepository.findByNomContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SessionDeFormationDTO addSoutenanceToSession(Long sessionId, Long soutenanceId) {
        SessionDeFormation session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found with id: " + soutenanceId));

        // Check if already associated
        if (session.getSoutenance().contains(soutenance)) {
            return convertToDTO(session); // Already added, just return
        }

        // Add association
        soutenance.setSessionDeFormation(session);
        session.getSoutenance().add(soutenance);

        return convertToDTO(sessionRepository.save(session));
    }

    @Override
    @Transactional
    public SessionDeFormationDTO removeSoutenanceFromSession(Long sessionId, Long soutenanceId) {
        SessionDeFormation session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found with id: " + soutenanceId));

        // Remove if associated
        if (session.getSoutenance().contains(soutenance)) {
            session.getSoutenance().remove(soutenance);
            soutenance.setSessionDeFormation(null);
            soutenanceRepository.save(soutenance);
        }

        return convertToDTO(sessionRepository.save(session));
    }

    // Helper method to convert entity to DTO
    private SessionDeFormationDTO convertToDTO(SessionDeFormation session) {
        // Calculate session status
        String status;
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(session.getDateDebut())) {
            status = "UPCOMING";
        } else if (now.isAfter(session.getDateFin())) {
            status = "COMPLETED";
        } else {
            status = "ACTIVE";
        }

        // Calculate duration in days
        long durationInDays = ChronoUnit.DAYS.between(
                session.getDateDebut().toLocalDate(),
                session.getDateFin().toLocalDate()
        ) + 1; // +1 to include both start and end day

        // Convert soutenances to DTOs
        List<SoutenanceDetailDTO> soutenanceDTOs = session.getSoutenance().stream()
                .map(this::convertToSoutenanceDTO)
                .collect(Collectors.toList());

        return SessionDeFormationDTO.builder()
                .id(session.getId())
                .nom(session.getNom())
                .information(session.getInformation())
                .dateDebut(session.getDateDebut())
                .dateFin(session.getDateFin())
                .soutenances(soutenanceDTOs)
                .status(status)
                .durationInDays(durationInDays)
                .build();
    }

    private SoutenanceDetailDTO convertToSoutenanceDTO(Soutenance soutenance) {
        return SoutenanceDetailDTO.builder()
                .id(soutenance.getId())
                .sujet(soutenance.getSujet())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                // Add other fields as needed
                .build();
    }
}