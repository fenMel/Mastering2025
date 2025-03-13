package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Memoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoireRepository extends JpaRepository<Memoire, Long> {
    List<Memoire> findByEtudiantId(Long etudiantId);
    List<Memoire> findBySoutenanceId(Long soutenanceId);
    Optional<Memoire> findByEtudiantIdAndSoutenanceId(Long etudiantId, Long soutenanceId);
}