package fr.esic.mastering.services;

import fr.esic.mastering.dto.MemoireDTO;
import fr.esic.mastering.dto.MemoireEvaluationRequest;
import fr.esic.mastering.dto.RevisionRequest;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoireService {
    private final MemoireRepository memoireRepository;
    private final ApprenantRepository apprenantRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final User_updateRepository user_updateRepository;

    @Value("${app.upload.memoire.dir}")
    private String memoireUploadDir;

    @PostConstruct
    public void init() {
        // Create base upload directory when service starts
        File baseDir = new File(memoireUploadDir);
        if (!baseDir.exists()) {
            if (baseDir.mkdirs()) {
                log.info("Created base upload directory: {}", baseDir.getAbsolutePath());
            } else {
                log.warn("Failed to create base upload directory: {}", baseDir.getAbsolutePath());
            }
        }
    }

    @Transactional
    public Memoire uploadMemoire(Long etudiantId, Long soutenanceId, String title, MultipartFile file) {
        try {
            // Find student and soutenance
            Apprenants etudiant = apprenantRepository.findById(etudiantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

            Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Soutenance non trouvée"));

            // Create student-specific directory
            // Create a student directory using a combination of first name and last name
            String studentDir = new File(memoireUploadDir + File.separator +
                    formatDirectoryName(etudiant.getPrenom(), etudiant.getNom())).getAbsolutePath();
            File directory = new File(studentDir);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    log.info("Created student directory: {}", directory.getAbsolutePath());
                } else {
                    throw new RuntimeException("Failed to create student directory: " + directory.getAbsolutePath());
                }
            }



            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;
            File targetFile = new File(directory, uniqueFilename);

            // Save file
            log.info("Attempting to save file to: {}", targetFile.getAbsolutePath());
            file.transferTo(targetFile);
            log.info("File saved successfully");

            // Create and save entity
            Memoire memoire = Memoire.builder()
                    .etudiant(etudiant)
                    .soutenance(soutenance)
                    .title(title)
                    .fileName(originalFilename)
                    .filePath(targetFile.getAbsolutePath())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .uploadDate(LocalDateTime.now())
                    .status(MemoireStatus.SUBMITTED)
                    .build();

            return memoireRepository.save(memoire);

        } catch (Exception e) {
            log.error("Error uploading mémoire: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload mémoire", e);
        }
    }
    public List<MemoireDTO> getEtudiantMemoires(Long etudiantId) {
        return memoireRepository.findByEtudiantId(etudiantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private String formatDirectoryName(String prenom, String nom) {
        if (prenom == null) prenom = "";
        if (nom == null) nom = "";

        // Combine first and last name, trim, and convert to lowercase
        String fullName = (prenom + " " + nom).trim().toLowerCase();

        // Remove accents
        String normalizedName = java.text.Normalizer
                .normalize(fullName, java.text.Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Replace non-alphanumeric characters with underscores
        String safeDirectoryName = normalizedName
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("_+", "_")
                .trim();

        // Ensure the directory name is not empty
        return safeDirectoryName.isEmpty() ? "unknown_student" : safeDirectoryName;
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
                .soutenanceId(memoire.getSoutenance().getId())
                .soutenanceSujet(memoire.getSoutenance().getSujet())
                .soutenanceDate(memoire.getSoutenance().getDateHeure())
                .build();
    }

    @Transactional
    public Memoire evaluateMemoire(Long memoireId, MemoireEvaluationRequest request) {
        Memoire memoire = memoireRepository.findById(memoireId)
                .orElseThrow(() -> new ResourceNotFoundException("Mémoire non trouvé"));

        User_update evaluator = user_updateRepository.findById(request.getEvaluatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Évaluateur non trouvé"));

        // Vérifier que l'utilisateur est bien membre du jury
        if (!hasJuryRole(evaluator)) {
            throw new AccessDeniedException("Seuls les membres du jury peuvent évaluer les mémoires");
        }

        // Mettre à jour le mémoire
        memoire.setNote(request.getNote());
        memoire.setMention(request.getMention());
        memoire.setAppreciation(request.getAppreciation());
        memoire.setEvaluator(evaluator);
        memoire.setEvaluationDate(LocalDateTime.now());
        memoire.setStatus(MemoireStatus.VALIDATED);

        Memoire updatedMemoire = memoireRepository.save(memoire);

        // Envoyer un email de notification à l'étudiant
        emailService.sendMemoireEvaluationNotification(
                updatedMemoire.getEtudiant(),
                updatedMemoire.getSoutenance(),
                updatedMemoire.getTitle(),
                updatedMemoire.getNote(),
                updatedMemoire.getMention(),
                updatedMemoire.getAppreciation()
        );

        return updatedMemoire;
    }

    @Transactional
public Memoire requestRevision(Long memoireId, RevisionRequest request) {
    Memoire memoire = memoireRepository.findById(memoireId)
            .orElseThrow(() -> new ResourceNotFoundException("Mémoire non trouvé"));

    User_update evaluator = user_updateRepository.findById(request.getEvaluatorId())
            .orElseThrow(() -> new ResourceNotFoundException("Évaluateur non trouvé"));

    // Vérifier que l'utilisateur est bien membre du jury
    if (!hasJuryRole(evaluator)) {
        throw new AccessDeniedException("Seuls les membres du jury peuvent demander des révisions");
    }

    // Mettre à jour le mémoire
    memoire.setRevisionComments(request.getRevisionComments());
    memoire.setEvaluator(evaluator);
    memoire.setEvaluationDate(LocalDateTime.now());
    memoire.setStatus(MemoireStatus.REJECTED);

    Memoire updatedMemoire = memoireRepository.save(memoire);

    // Envoyer un email de notification à l'étudiant
    emailService.sendMemoireRevisionNotification(
            updatedMemoire.getEtudiant(),
            updatedMemoire.getSoutenance(),
            updatedMemoire.getTitle(),
            updatedMemoire.getRevisionComments()
    );

    return updatedMemoire;
}



    private boolean hasJuryRole(User_update user ) {
        // Implémenter la vérification du rôle
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_JURY"));
    }
}
