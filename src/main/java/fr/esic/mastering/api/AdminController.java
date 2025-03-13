package fr.esic.mastering.api;


import fr.esic.mastering.dto.*;
import fr.esic.mastering.entities.Apprenants;
import fr.esic.mastering.entities.Soutenance;
import fr.esic.mastering.entities.User_update;
import fr.esic.mastering.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;
    private final SoutenanceService soutenanceService;
    private final ApprenantService etudiantService; // Service to access student data

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboardInfo() {
        log.info("Retrieving dashboard information");

        // Get all students (not using roles since they don't have ROLE_ETUDIANT)
        List<EtudiantDTO> students = etudiantService.getAllEtudiantsWithDetails()
                .stream()
                .map(etudiantWithDetails -> EtudiantDTO.builder()
                        .id(etudiantWithDetails.getId())
                        .nom(etudiantWithDetails.getNom())
                        .prenom(etudiantWithDetails.getPrenom())
                        .email(etudiantWithDetails.getEmail())
                        // Map any other fields from your EtudiantWithDetails object
                        .build())
                .collect(Collectors.toList());

        // Get users with specific roles
        List<UserDTO> juryMembers = userService.getUsersByRole("ROLE_JURY")
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());

        List<UserDTO> coordinateurs = userService.getUsersByRole("ROLE_COORDONATEUR")
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());

        // Get all soutenances with details
        List<SoutenanceDetailDTO> soutenances = soutenanceService.getAllSoutenances()
                .stream()
                .map(this::convertToSoutenanceDetailDTO)
                .collect(Collectors.toList());

        // Build the dashboard response
        DashboardDTO dashboardDTO = DashboardDTO.builder()
                .students(students)
                .juryMembers(juryMembers)
                .coordinateurs(coordinateurs)
                .soutenances(soutenances)
                .counts(CountsDTO.builder()
                        .totalStudents(students.size())
                        .totalJuryMembers(juryMembers.size())
                        .totalCoordinateurs(coordinateurs.size())
                        .totalSoutenances(soutenances.size())
                        .build())
                .build();

        return ResponseEntity.ok(dashboardDTO);
    }

    private UserDTO convertToUserDTO(User_update user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    private EtudiantDTO convertToEtudiantDTO(Apprenants etudiant) {
        return EtudiantDTO.builder()
                .id(etudiant.getId())
                .nom(etudiant.getNom())
                .prenom(etudiant.getPrenom())
                .email(etudiant.getEmail())
                // Add any other student-specific fields
                .build();
    }

    private SoutenanceDetailDTO convertToSoutenanceDetailDTO(Soutenance soutenance) {
        return SoutenanceDetailDTO.builder()
                .id(soutenance.getId())
                .sujet(soutenance.getSujet())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                .etudiants(soutenance.getInscriptions().stream()
                        .map(inscription -> ApprenantDetailDTO.builder()
                                .id(inscription.getEtudiant().getId())
                                .nom(inscription.getEtudiant().getNom())
                                .prenom(inscription.getEtudiant().getPrenom())
                                .email(inscription.getEtudiant().getEmail())
                                .build())
                        .collect(Collectors.toList()))
                .juryMembers(soutenance.getJuryMembers().stream()
                        .map(this::convertToUserDTO)
                        .collect(Collectors.toList()))
                .coordinateur(soutenance.getCoordinateur() != null ?
                        convertToUserDTO(soutenance.getCoordinateur()) : null)
                .build();
    }
}