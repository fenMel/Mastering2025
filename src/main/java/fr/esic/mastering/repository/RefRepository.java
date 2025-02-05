package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Referentiels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefRepository extends JpaRepository<Referentiels, Long> {
    List<Referentiels> findByFormation_Id(Long formationId);
    
        
    }

