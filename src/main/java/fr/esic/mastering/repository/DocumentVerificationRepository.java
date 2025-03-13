package fr.esic.mastering.repository;

import fr.esic.mastering.entities.DocumentStatus;
import fr.esic.mastering.entities.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
    List<DocumentVerification> findByStatus(DocumentStatus status);
}
