package fr.esic.mastering.repository;

import fr.esic.mastering.entities.SessionDeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionDeFormationRepository extends JpaRepository<SessionDeFormation, Long> {
    // Find sessions by name (useful for searching)
    List<SessionDeFormation> findByNomContainingIgnoreCase(String nom);

    // Find active sessions (current date is between dateDebut and dateFin)
    List<SessionDeFormation> findByDateDebutBeforeAndDateFinAfter(
            LocalDateTime currentDate, LocalDateTime sameCurrentDate);

    // Find upcoming sessions
    List<SessionDeFormation> findByDateDebutAfterOrderByDateDebutAsc(LocalDateTime currentDate);

    // Find completed sessions
    List<SessionDeFormation> findByDateFinBeforeOrderByDateFinDesc(LocalDateTime currentDate);
}