package fr.esic.mastering.services;

import fr.esic.mastering.dto.ReferentielDTO;
import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.Referentiels;
import fr.esic.mastering.repository.FormationRepository;
import fr.esic.mastering.repository.RefRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReferentielService {
    private final RefRepository refRepository;
    private final FormationRepository formationRepository;

    // Injection des repositories
    public ReferentielService(RefRepository refRepository, FormationRepository formationRepository) {
        this.refRepository = refRepository;
        this.formationRepository = formationRepository;
    }

    // Récupérer tous les référentiels
    public List<Referentiels> getAllReferentiels() {
        return refRepository.findAll();
    }

    // Récupérer un référentiel par ID
    public Referentiels getReferentielById(Long id) {
        Optional<Referentiels> referentiels = refRepository.findById(id);
        return referentiels.orElseThrow(() -> new RuntimeException("Referentiel not found with id: " + id));
    }

    // Créer un référentiel à partir du DTO
    public Referentiels createReferentiel(ReferentielDTO referentielDTO) {
        Formation formation = formationRepository.findById(referentielDTO.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + referentielDTO.getFormationId()));

        Referentiels referentiels = new Referentiels();
        referentiels.setNom(referentielDTO.getNom());
        referentiels.setDescription(referentielDTO.getDescription());
        referentiels.setFormation(formation); // Associer la formation

        return refRepository.save(referentiels);
    }

    // Mettre à jour un référentiel
    public Referentiels updateReferentiel(Long id, ReferentielDTO referentielDTO) {
        Referentiels referentiels = refRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found with id: " + id));

        Formation formation = formationRepository.findById(referentielDTO.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + referentielDTO.getFormationId()));

        referentiels.setNom(referentielDTO.getNom());
        referentiels.setDescription(referentielDTO.getDescription());
        referentiels.setFormation(formation); // Associer la formation

        return refRepository.save(referentiels);
    }

    // Supprimer un référentiel par ID
    @Transactional
    public void deleteReferentiel(Long id) {
        Referentiels referentiels = refRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found with id: " + id));

        refRepository.delete(referentiels);
    }

    // Récupérer les référentiels d'une formation spécifique
    public List<Referentiels> getReferentielsByFormationId(Long formationId) {
        return refRepository.findByFormation_Id(formationId);
    }
}
