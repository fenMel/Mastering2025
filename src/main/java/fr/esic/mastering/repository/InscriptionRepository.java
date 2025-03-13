package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Apprenants;
import fr.esic.mastering.entities.Inscription;
import fr.esic.mastering.entities.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findBySoutenanceId(Long soutenanceId);
    List<Inscription> findByEtudiantEmail(String email);

    // Nouvelle méthode pour récupérer les étudiants d'une session
    @Query("SELECT i.etudiant FROM Inscription i WHERE i.soutenance.id = :soutenanceId")
    List<Apprenants> findEtudiantsBySoutenanceId(@Param("soutenanceId") Long soutenanceId);

    boolean existsBySoutenanceAndEtudiant(Soutenance soutenance, Apprenants etudiant);
    List<Inscription> findBySoutenanceIdAndEtudiantId(Long soutenanceId, Long etudiantId);

    List<Inscription> findAllBySoutenance(Soutenance soutenance);
}

