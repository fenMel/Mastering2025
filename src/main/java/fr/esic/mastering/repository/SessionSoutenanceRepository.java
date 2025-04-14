package fr.esic.mastering.repository;

import fr.esic.mastering.entities.SessionSoutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionSoutenanceRepository extends JpaRepository<SessionSoutenance, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si nécessaire
    // Par exemple : 
    // List<SessionSoutenance> findByResponsable(String responsable);
}
