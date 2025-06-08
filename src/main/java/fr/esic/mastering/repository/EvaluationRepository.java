package fr.esic.mastering.repository;


import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCandidatId(Long candidatId);

    List<Evaluation> findByJuryId(Long juryId);

}
