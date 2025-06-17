package fr.esic.mastering.services;

import fr.esic.mastering.entities.ArchiveDecision;
import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.VerdictDecision;
import fr.esic.mastering.repository.ArchiveDecisionRepository;
import fr.esic.mastering.repository.DecisionRepository;
import fr.esic.mastering.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des décisions prises sur les évaluations des candidats.
 * Ce service permet d'ajouter, consulter et supprimer des décisions, avec logique de calcul de moyenne et de verdict.
 */
@Service
public class DecisionService {

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private ArchiveDecisionRepository archiveDecisionRepository;

    /**
     * Ajoute ou met à jour une décision pour un candidat donné, en se basant sur les moyennes de ses évaluations.
     *
     * @param candidatId       L'identifiant du candidat concerné
     * @param juryId           L'identifiant du jury (optionnel)
     * @param commentaireFinal Le commentaire final à associer à la décision
     * @return L'objet Decision sauvegardé (nouveau ou mis à jour)
     * @throws RuntimeException si aucune évaluation n'existe pour le candidat
     */
    public Decision addDecision(Long candidatId, Long juryId, String commentaireFinal) {
        List<Evaluation> evaluations = evaluationRepository.findByCandidatId(candidatId);

        if (evaluations.isEmpty()) {
            throw new RuntimeException("Aucune évaluation trouvée pour le candidat ID : " + candidatId);
        }

        // Calcul de la moyenne globale du candidat à partir de toutes ses évaluations
        double moyenne = evaluations.stream()
                .mapToDouble(Evaluation::calculerMoyenne)
                .average()
                .orElse(0);

        // Détermination du verdict selon les seuils définis
        VerdictDecision verdict;
        if (moyenne >= 10) {
            verdict = VerdictDecision.ADMIS;
        } else if (moyenne >= 9) {
            verdict = VerdictDecision.RATTRAPAGE;
        } else {
            verdict = VerdictDecision.NON_ADMIS;
        }

        // On se base sur la première évaluation comme référence pour créer ou mettre à jour une décision
        Evaluation evaluation = evaluations.get(0);

        // Vérifie si une décision existe déjà pour cette évaluation
        Optional<Decision> existing = decisionRepository.findByEvaluation_Id(evaluation.getId());
        if (existing.isPresent()) {
            Decision decision = existing.get();
            decision.setVerdict(verdict);
            decision.setCommentaireFinal(commentaireFinal);
            return decisionRepository.save(decision);
        } else {
            Decision decision = new Decision();
            decision.setCandidat(evaluation.getCandidat());
            decision.setJury(juryId != null ? evaluation.getJury() : null);
            decision.setEvaluation(evaluation);
            decision.setCommentaireFinal(commentaireFinal);
            decision.setVerdict(verdict);
            return decisionRepository.save(decision);
        }
    }

    /**
     * Récupère la liste de toutes les décisions enregistrées.
     *
     * @return Liste des décisions
     */
    public List<Decision> getAllDecisions() {
        return decisionRepository.findAll();
    }

    /**
     * Récupère toutes les décisions associées à un candidat spécifique.
     *
     * @param candidatId Identifiant du candidat
     * @return Liste des décisions liées à ce candidat
     */
    public List<Decision> getDecisionsByCandidat(Long candidatId) {
        return decisionRepository.findByCandidatId(candidatId);
    }


    /**
     * Supprime une décision existante par son identifiant.
     *
     * @param id Identifiant de la décision à supprimer
     */
    /**
     * Supprime une décision existante par son identifiant.
     *
     * @param id Identifiant de la décision à supprimer
     * @param archivePar Email de l'utilisateur qui effectue l'archivage
     */
    public void deleteDecision(Long id, String archivePar) {
        System.out.println("Début de la suppression de la décision avec l'ID : " + id);

        Decision decision = decisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Décision non trouvée"));
        System.out.println("Décision trouvée pour l'ID : " + id);

        ArchiveDecision archive = new ArchiveDecision();
        archive.setId(decision.getId());

        // Archivage des informations du candidat avec nom et prénom
        if (decision.getCandidat() != null) {
            archive.setCandidatId(decision.getCandidat().getId());
            archive.setCandidatNom(decision.getCandidat().getNom());         // NOUVEAU
            archive.setCandidatPrenom(decision.getCandidat().getPrenom());   // NOUVEAU
            System.out.println("Archivage des données du candidat : " +
                    decision.getCandidat().getPrenom() + " " + decision.getCandidat().getNom());
        }

        // Archivage des informations du jury avec nom et prénom
        if (decision.getJury() != null) {
            archive.setJuryId(decision.getJury().getId());
            archive.setJuryNom(decision.getJury().getNom());                 // NOUVEAU
            archive.setJuryPrenom(decision.getJury().getPrenom());           // NOUVEAU
            System.out.println("Archivage des données du jury : " +
                    decision.getJury().getPrenom() + " " + decision.getJury().getNom());
        }

        archive.setMoyenne(decision.getEvaluation().getMoyenne());
        System.out.println("Moyenne archivée : " + decision.getEvaluation().getMoyenne());

        archive.setCommentaire(decision.getCommentaireFinal());
        if (decision.getVerdict() != null) {
            archive.setVerdict(decision.getVerdict().toString());
            System.out.println("Verdict archivé : " + decision.getVerdict().toString());
        }

        archive.setDateArchivage(LocalDateTime.now());

        // Extraction du nom et prénom à partir de l'email et sauvegarde du nom complet
        String nomComplet = archivePar;
        if (archivePar != null && archivePar.contains("@")) {
            try {
                String[] nameParts = archivePar.split("@")[0].split("\\.");
                String nom = nameParts.length > 1 ? nameParts[1].substring(0, 1).toUpperCase() + nameParts[1].substring(1) : "";
                String prenom = nameParts.length > 0 ? nameParts[0].substring(0, 1).toUpperCase() + nameParts[0].substring(1) : "";

                if (!prenom.isEmpty() && !nom.isEmpty()) {
                    nomComplet = prenom + " " + nom;
                } else if (!prenom.isEmpty()) {
                    nomComplet = prenom;
                } else if (!nom.isEmpty()) {
                    nomComplet = nom;
                }

                System.out.println("Archivage effectué par : " + nomComplet);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'extraction du nom/prénom, utilisation de l'email : " + archivePar);
                nomComplet = archivePar;
            }
        }

        archive.setArchivePar(nomComplet);

        archiveDecisionRepository.save(archive);
        System.out.println("Archive sauvegardée avec succès");

        decisionRepository.deleteById(id);
        System.out.println("Décision supprimée avec succès");
    }
}
