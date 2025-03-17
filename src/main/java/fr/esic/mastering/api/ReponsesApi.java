package fr.esic.mastering.api;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.services.QuestionsService;
import fr.esic.mastering.services.ReponsesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/responses")

public class ReponsesApi {
	 @Autowired
	    private ReponsesService responseService;
	 @Autowired
	    private QuestionsService questionsService;

	    // Endpoint pour enregistrer une réponse
	 @PostMapping("/{questionId}")
	    public void saveResponse(@PathVariable Long questionId, @RequestBody String answer) {
	        // Récupérer la question par son id
	        Questions question = questionsService.getQuestionById(questionId);

	        if (question != null) {
	            // Créer une nouvelle réponse et associer la question
	            Reponses reponse = new Reponses();
	            reponse.setQuestion(question);
	            reponse.setAnswer(answer);

	            // Enregistrer la réponse
	            responseService.saveReponse(reponse);
	        } else {
	            throw new IllegalArgumentException("Question avec ID " + questionId + " non trouvée");
	        }
	    }
	    // Endpoint pour récupérer toutes les réponses
	    @GetMapping
	    public List<Reponses> getAllResponses() {
	        return responseService.getAllResponses();
	    }
}
