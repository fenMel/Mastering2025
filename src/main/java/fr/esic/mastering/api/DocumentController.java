package fr.esic.mastering.api;

import fr.esic.mastering.dto.DocumentDTO;
import fr.esic.mastering.entities.Document;
import fr.esic.mastering.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
private final DocumentService documentService;

    @PostMapping(value = "/upload/{etudiantId}", consumes = "multipart/form-data")
    public ResponseEntity<Document> uploadDocument(
            @PathVariable Long etudiantId,
            @RequestParam("type") String documentType,
            @RequestParam("file") MultipartFile file
    ) {
        Document document = documentService.uploadDocument(etudiantId, documentType, file);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/student/{etudiantId}")
    public ResponseEntity<List<DocumentDTO>> getStudentDocuments(@PathVariable Long etudiantId) {
        List<Document> documents = documentService.getStudentDocuments(etudiantId);
        List<DocumentDTO> documentDTOs = documents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(documentDTOs);
    }

    private DocumentDTO convertToDTO(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .documentType(document.getDocumentType())
                .fileName(document.getFileName())
                .uploadDate(document.getUploadDate())
                .build();
    }
}
