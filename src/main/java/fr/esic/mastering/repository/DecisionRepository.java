package fr.esic.mastering.repository;


import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.VerdictDecision;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends JpaRepository<Decision, Long> {
    List<Decision> findByCandidatId(Long candidatId);
    List<Decision> findByJuryId(Long juryId);
    List<Decision> findByVerdict(VerdictDecision verdict);

    Optional<Decision> findByEvaluation_Id(Long evaluationId);
}
