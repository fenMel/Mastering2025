package fr.esic.mastering.repository;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation, Long> {


    Optional<Formation> findByNom(String nom);

}
