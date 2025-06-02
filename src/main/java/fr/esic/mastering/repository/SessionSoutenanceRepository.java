package fr.esic.mastering.repository;

import fr.esic.mastering.entities.SessionSoutenance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionSoutenanceRepository extends JpaRepository<SessionSoutenance, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si nécessaire
    // Par exemple : 
    // List<SessionSoutenance> findByResponsable(String responsable);

    //  @Query("SELECT ss FROM SessionSoutenance ss JOIN FETCH ss.sessionFormation sf LEFT JOIN FETCH sf.users")
    // List<SessionSoutenance> findAllWithUsers();
}
