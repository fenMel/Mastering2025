package fr.esic.mastering.services;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.repository.FormationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormationService {
    private final FormationRepository formationRepository;

    // Injection du repository
    public FormationService(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
    }

    // Récupérer toutes les formations
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    // Récupérer une formation par ID
    public Formation getFormationById(Long id) {
        Optional<Formation> formation = formationRepository.findById(id);
        return formation.orElseThrow(() -> new RuntimeException("Formation not found with id: " + id));
    }

    // Créer une nouvelle formation
    public Formation createFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    // Mettre à jour une formation
    public Formation updateFormation(Long id, Formation formation) {
        Formation existingFormation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + id));

        existingFormation.setNom(formation.getNom());
        existingFormation.setDescription(formation.getDescription());
        return formationRepository.save(existingFormation);
    }

    // Supprimer une formation
    public void deleteFormation(Long id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + id));

        formationRepository.delete(formation);
    }
}
