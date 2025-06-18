package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {


    Optional<Formation> findByNom(String nom);
@EntityGraph(attributePaths = "sessionsFormation")
Optional<Formation> findById(Long id);
}
