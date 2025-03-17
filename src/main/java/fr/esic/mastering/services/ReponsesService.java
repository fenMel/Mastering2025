package fr.esic.mastering.services;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReponsesService {
	@Autowired
    private ReponsesRepository reponseRepository;
	@Autowired
    private ReponsesRepository reponsesRepository;
    // Enregistre une réponse
    public void saveResponse(Reponses response) {
        reponseRepository.save(response);
    }

    // Récupère toutes les réponses
    public List<Reponses> getAllResponses() {
        return reponseRepository.findAll();
    }

    public void saveReponse(Reponses reponse) {
        reponsesRepository.save(reponse);
    }
}
