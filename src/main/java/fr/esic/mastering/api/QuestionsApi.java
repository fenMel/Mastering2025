package fr.esic.mastering.api;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.services.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/questions")
public class QuestionsApi {
	@Autowired
    private QuestionsService questionService;

    // Endpoint pour récupérer toutes les questions
    @GetMapping
    public List<Questions> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Endpoint pour créer les questions par défaut
    @PostMapping("/default")
    public void createDefaultQuestions() {
        questionService.createDefaultQuestions();
    }
}
