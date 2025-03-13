package fr.esic.mastering.services;

import fr.esic.mastering.entities.Apprenants;
import fr.esic.mastering.entities.Document;
import fr.esic.mastering.entities.DocumentType;
import fr.esic.mastering.entities.StatutInscription;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.repository.ApprenantRepository;
import fr.esic.mastering.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ApprenantRepository apprenantsRepository;
    private final EmailService emailService;
    private final VerificationService verification;

    @Value("${app.upload.dir}")
    private String uploadDir;
    private long maxFileSize;


    public Document uploadDocument(Long etudiantId, String documentType, MultipartFile file) {
        try {
            // Find student
            Apprenants etudiant = apprenantsRepository.findById(etudiantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            // Create base upload directory if it doesn't exist
            File baseDirectory = new File(uploadDir);
            if (!baseDirectory.exists()) {
                baseDirectory.mkdirs();
            }

            // Create student-specific directory
            File studentDirectory = new File(baseDirectory, etudiantId.toString());
            if (!studentDirectory.exists()) {
                studentDirectory.mkdirs();
            }

            // Generate unique filename
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = studentDirectory.toPath().resolve(fileName);

            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save document info in database
            Document document = Document.builder()
                    .etudiant(etudiant)
                    .documentType(documentType)
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .uploadDate(LocalDateTime.now())
                    .build();

            Document savedDocument = documentRepository.save(document);



            // Check if all required documents are uploaded
            checkAndUpdateStudentStatus(etudiant, document);

            return savedDocument;

        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    @Transactional
    public void checkAndUpdateStudentStatus(Apprenants etudiant ,Document document) {
        boolean hasSchoolCertificate = documentRepository
                .existsByEtudiantIdAndDocumentType(etudiant.getId(), DocumentType.SCHOOL_CERTIFICATE.name());
        boolean hasIdDocument = documentRepository
                .existsByEtudiantIdAndDocumentType(etudiant.getId(), DocumentType.ID_DOCUMENT.name());

        if (hasSchoolCertificate && hasIdDocument) {
            etudiant.setStatus(StatutInscription.EN_VERIFICATION);  // Using existing enum
            apprenantsRepository.save(etudiant);

            // Send email notification
            emailService.sendProfileUnderVerificationEmail(etudiant, document);

            log.info("Profile status updated to EN_VERIFICATION for student: {}", etudiant.getEmail());
        }

    }

    public List<Document> getStudentDocuments(Long etudiantId) {
        return documentRepository.findByEtudiantId(etudiantId);
    }
}


