package fr.esic.mastering.api;
import fr.esic.mastering.dto.*;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.repository.InscriptionRepository;
import fr.esic.mastering.services.SoutenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/soutenances")
@RequiredArgsConstructor
@Slf4j
public class SoutenanceController {
    private final SoutenanceService soutenanceService;
    private final InscriptionRepository inscriptionRepository;

    @PostMapping
    public ResponseEntity<Soutenance> creerSoutenance(@Valid @RequestBody SoutenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(soutenanceService.creerSoutenance(request));
    }

    @PostMapping("/{soutenanceId}/inscriptions")
    public ResponseEntity<Inscription> inscrireApprenant(
            @PathVariable Long soutenanceId,
            @Valid @RequestBody InscriptionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(soutenanceService.inscrireEtudiant(soutenanceId, request));
    }


    @GetMapping("/{soutenanceId}")
    @Transactional
    public ResponseEntity<SoutenanceDetailRequest> getSoutenanceById(@PathVariable Long soutenanceId) {
        Soutenance soutenance = soutenanceService.getSoutenanceById(soutenanceId);
        SoutenanceDetailRequest dto = SoutenanceDetailRequest.builder()
                .id(soutenance.getId())
                .sujet(soutenance.getSujet())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                .etudiants(getEtudiantsWithStatus(soutenance.getId()))
                .membreJury(soutenance.getJuryMembers().stream()
                        .map(juryMember -> JuryMemberRequest.builder()
                                .nom(juryMember.getNom())
                                .prenom(juryMember.getPrenom())
                                .email(juryMember.getEmail())
                                .build())

                        .toList())
                .coordinateur(soutenance.getCoordinateur() != null ? List.of(CoordinateurEssentialRequest.builder()
                        .username(soutenance.getCoordinateur().getUsername())
                        .nom(soutenance.getCoordinateur().getNom())
                        .prenom(soutenance.getCoordinateur().getPrenom())
                        .email(soutenance.getCoordinateur().getEmail())
                        .build()) : List.of())
                .build();
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{soutenanceId}")
    @Transactional
    public ResponseEntity<?> updateSoutenance(
            @PathVariable Long soutenanceId,
            @Valid @RequestBody SoutenanceRequest request) {
        Soutenance updatedSoutenance = soutenanceService.updateSoutenance(soutenanceId, request);
        SoutenanceDetailRequest response = convertToDTO(updatedSoutenance);
        return ResponseEntity.ok(response);
    }

    private SoutenanceDetailRequest convertToDTO(Soutenance soutenance) {
        return SoutenanceDetailRequest.builder()
                .id(soutenance.getId())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                .sujet(soutenance.getSujet())
                .build();
    }




    @GetMapping("/{soutenanceId}/etudiants")
    public ResponseEntity<List<SessionApparentRequest>> getEtudiantsBySoutenance(
            @PathVariable Long soutenanceId
    ) {
        List<Apprenants> etudiants = soutenanceService.getEtudiantsBySoutenanceId(soutenanceId);

        List<SessionApparentRequest> dtos = etudiants.stream()
                .map(e -> SessionApparentRequest.builder()
                        .id(e.getId())
                        .nom(e.getNom())
                        .prenom(e.getPrenom())
                        .email(e.getEmail())
                        .statut(e.getStatus()) // Méthode à implémenter
                        .build())
                        .toList();

        return ResponseEntity.ok(dtos);
    }

    private StatutInscription getStatutInscription(Long soutenanceId, Long etudiantId) {
        List<Inscription> inscriptions = inscriptionRepository.findBySoutenanceIdAndEtudiantId(soutenanceId, etudiantId);
        return inscriptions.isEmpty() ?
                StatutInscription.APPRENANT :
                inscriptions.get(0).getStatut();  // Or implement logic to determine which status to return if multiple exist
    }


    @GetMapping
    public ResponseEntity<List<SoutenanceDetailRequest>> getAllSoutenances() {
        List<Soutenance> soutenances = soutenanceService.getAllSoutenances();

        List<SoutenanceDetailRequest> response = soutenances.stream()
                .map(soutenance -> SoutenanceDetailRequest.builder()
                        .id(soutenance.getId())
                        .sujet(soutenance.getSujet())
                        .lieu(soutenance.getLieu())
                        .dateHeure(soutenance.getDateHeure())
                        .etudiants(getEtudiantsWithStatus(soutenance.getId()))
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }



    private List<SessionApparentRequest> getEtudiantsWithStatus(Long soutenanceId) {
        List<Apprenants> etudiants = soutenanceService.getEtudiantsBySoutenanceId(soutenanceId);

        return etudiants.   stream()
                .map(e -> SessionApparentRequest.builder()
                        .id(e.getId())
                        .nom(e.getNom())
                        .prenom(e.getPrenom())
                        .email(e.getEmail())
                        .statut(e.getStatus())
                        .build())
                .toList();
    }

    @PostMapping("/{soutenanceId}/jury")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoutenanceDetailRequest> assignJury(
            @PathVariable Long soutenanceId,
            @Valid @RequestBody JuryAssignmentRequest request
    ) {
        log.info("Assigning jury to soutenance: {}", soutenanceId);
        Soutenance soutenance = soutenanceService.assignJury(soutenanceId, request.getJuryIds());

        SoutenanceDetailRequest dto = SoutenanceDetailRequest.builder()
                .id(soutenance.getId())
                .sujet(soutenance.getSujet())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                .etudiants(getEtudiantsWithStatus(soutenance.getId()))
                .build();

        return ResponseEntity.ok(dto);
    }


    //Asigner un coordonateur

    @PostMapping("/{soutenanceId}/coordinateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoutenanceDetailRequest> assignCoordinateur(
            @PathVariable Long soutenanceId,
            @Valid @RequestBody CoordinateurAssignmentRequest request
    ) {
        log.info("Assigning coordinateur to soutenance: {}", soutenanceId);
        Soutenance soutenance = soutenanceService.assignCoordinateur(soutenanceId, request.getCoordinateurId());

        SoutenanceDetailRequest dto = SoutenanceDetailRequest.builder()
                .id(soutenance.getId())
                .sujet(soutenance.getSujet())
                .lieu(soutenance.getLieu())
                .dateHeure(soutenance.getDateHeure())
                .coordinateur(soutenance.getCoordinateur() != null ?
                    List.of(CoordinateurEssentialRequest.builder()
                        .nom(soutenance.getCoordinateur().getNom())
                        .prenom(soutenance.getCoordinateur().getPrenom())
                        .email(soutenance.getCoordinateur().getEmail())
                        .build())
                    : List.of())
                .etudiants(getEtudiantsWithStatus(soutenance.getId()))
                .build();

        return ResponseEntity.ok(dto);
    }

    private UserDTO convertToUserDTO(User_update user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role_update::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}


