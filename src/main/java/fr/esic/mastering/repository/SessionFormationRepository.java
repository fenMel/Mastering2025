package fr.esic.mastering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.entities.User;
@Repository
public interface SessionFormationRepository extends JpaRepository<SessionFormation, Long> {
   


@Query("SELECT DISTINCT s FROM SessionFormation s LEFT JOIN FETCH s.formation LEFT JOIN FETCH s.candidats")
List<SessionFormation> findAllWithFormationAndCandidats();

}
