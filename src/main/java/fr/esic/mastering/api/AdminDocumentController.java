package fr.esic.mastering.api;

import fr.esic.mastering.dto.CommentRequest;
import fr.esic.mastering.dto.DocumentVerificationRequest;
import fr.esic.mastering.dto.DocumentWithStudentDTO;
import fr.esic.mastering.dto.VerificationCommentDTO;
import fr.esic.mastering.entities.DocumentVerification;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.services.DocumentVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {
    private final DocumentVerificationService verificationService;

    @GetMapping("/pending")
    public ResponseEntity<List<DocumentWithStudentDTO>> getPendingDocuments() {  // Update return type
        List<DocumentWithStudentDTO> documents = verificationService.getPendingDocuments();
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/{documentId}/verify")
    public ResponseEntity<DocumentVerification> verifyDocument(
            @PathVariable Long documentId,
            @RequestBody @Valid DocumentVerificationRequest request
    ) {
        return ResponseEntity.ok(verificationService.verifyDocument(documentId, request));
    }

    @PostMapping("/verification/{verificationId}/comment")
    public ResponseEntity<VerificationCommentDTO> addComment(
            @PathVariable Long verificationId,
            @RequestBody @Valid CommentRequest request
    ) {
        try {
            VerificationCommentDTO comment = verificationService.addComment(verificationId, request);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}