package fr.esic.mastering.api;

import fr.esic.mastering.dto.MemoireDTO;
import fr.esic.mastering.dto.MemoireEvaluationRequest;
import fr.esic.mastering.dto.RevisionRequest;
import fr.esic.mastering.entities.Memoire;
import fr.esic.mastering.services.MemoireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/memoires")
@RequiredArgsConstructor
@Slf4j
public class MemoireController {
    private final MemoireService memoireService;

    @PostMapping("/upload/{etudiantId}")
    public ResponseEntity<Memoire> uploadMemoire(
            @PathVariable Long etudiantId,
            @RequestParam("soutenance") Long soutenanceId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("Demande de dépôt de mémoire pour l'étudiant: {}, soutenance: {}", etudiantId, soutenanceId);
        Memoire memoire = memoireService.uploadMemoire(etudiantId, soutenanceId, title, file);
        return ResponseEntity.ok(memoire);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<MemoireDTO>> getEtudiantMemoires(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(memoireService.getEtudiantMemoires(etudiantId));
    }

    @PutMapping("/{memoireId}/evaluate")
    @PreAuthorize("hasRole('JURY')")
    public ResponseEntity<MemoireDTO> evaluateMemoire(
            @PathVariable Long memoireId,
            @Valid @RequestBody MemoireEvaluationRequest request
    ) {
        log.info("Évaluation du mémoire: {}", memoireId);
        Memoire memoire = memoireService.evaluateMemoire(memoireId, request);
        return ResponseEntity.ok(convertToDTO(memoire));
    }

    @PutMapping("/{memoireId}/revision")
    @PreAuthorize("hasRole('JURY')")
    public ResponseEntity<MemoireDTO> requestRevision(
            @PathVariable Long memoireId,
            @Valid @RequestBody RevisionRequest request
    ) {
        log.info("Demande de révision pour le mémoire: {}", memoireId);
        Memoire memoire = memoireService.requestRevision(memoireId, request);
        return ResponseEntity.ok(convertToDTO(memoire));
    }


    private MemoireDTO convertToDTO(Memoire memoire) {
        return MemoireDTO.builder()
                .id(memoire.getId())
                .title(memoire.getTitle())
                .fileName(memoire.getFileName())
                .fileType(memoire.getFileType())
                .fileSize(memoire.getFileSize())
                .uploadDate(memoire.getUploadDate())
                .status(memoire.getStatus())
                .note(memoire.getNote())
                .mention(memoire.getMention())
                .appreciation(memoire.getAppreciation())
                .revisionComments(memoire.getRevisionComments())
                .evaluationDate(memoire.getEvaluationDate())
                .evaluatorName(memoire.getEvaluator() != null ?
                        memoire.getEvaluator().getNom() + " " + memoire.getEvaluator().getPrenom() : null)
                .soutenanceId(memoire.getSoutenance().getId())
                .soutenanceSujet(memoire.getSoutenance().getSujet())
                .soutenanceDate(memoire.getSoutenance().getDateHeure())
                .build();
    }
}
