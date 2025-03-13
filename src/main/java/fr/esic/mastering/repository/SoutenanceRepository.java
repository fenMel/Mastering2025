package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
    @Transactional
    @Query("UPDATE Soutenance s SET s.coordinateur.id = :coordinateurId WHERE s.id = :soutenanceId")
    void assignCoordinateur(@Param("soutenanceId") Long soutenanceId, @Param("coordinateurId") Long coordinateurId);
}
