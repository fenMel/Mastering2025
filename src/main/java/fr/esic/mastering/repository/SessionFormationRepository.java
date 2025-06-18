package fr.esic.mastering.repository;


import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.SessionFormation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Ajoute cette annotation si elle n'est pas présente
public interface SessionFormationRepository extends JpaRepository<SessionFormation, Long> {

    Optional<SessionFormation> findByTitre(String titre);
    // Tu peux ajouter des méthodes spécifiques ici si nécessaire 
    void deleteByFormation(Formation formation);

    void deleteByFormationId(Long formationId);
    
}


