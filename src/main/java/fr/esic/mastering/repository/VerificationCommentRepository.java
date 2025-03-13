package fr.esic.mastering.repository;

import fr.esic.mastering.entities.VerificationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCommentRepository extends JpaRepository<VerificationComment, Long> {
}