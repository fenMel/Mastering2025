package fr.esic.mastering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.FormationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    // Méthode pour obtenir toutes les formations
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }


    // Méthode pour obtenir une formation par son ID
    public Optional<Formation> getFormationById(Long id) {
        return formationRepository.findById(id);
    }

    // Méthode pour créer une nouvelle formation
    public Formation createFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    // Méthode pour supprimer une formation par son ID
    public void deleteFormation(Long id) {
        formationRepository.deleteById(id);
    }
}
