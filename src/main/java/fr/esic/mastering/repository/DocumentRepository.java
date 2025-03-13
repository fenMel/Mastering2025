package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Document;
import fr.esic.mastering.entities.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEtudiantId(Long etudiantId);
    boolean existsByEtudiantIdAndDocumentType(Long etudiantId, String documentType);
    List<Document> findByStatus(DocumentStatus status);

}
