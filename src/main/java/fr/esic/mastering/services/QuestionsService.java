package fr.esic.mastering.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;


import fr.esic.mastering.entities.*;
import fr.esic.mastering.repository.QuestionsRepository;
@Service
@SpringBootApplication
public class QuestionsService implements CommandLineRunner {
	  @Autowired
	    private QuestionsRepository questionsRepository;

	public void createDefaultQuestions () {
		//SpringApplication.run(Spring1Application.class);
		  
		    

	    // Liste des questions à créer
	    List<Questions> questions = Arrays.asList(
	        // Question avec choix multiples de 1 à 5
	        new Questions("Notez l'organisation de 1 à 5", Arrays.asList("1", "2", "3", "4", "5"), true),

	        // Question avec un choix Oui/Non
	        new Questions("Le jury était-il clair dans ses explications ?", Arrays.asList("Oui", "Non"), true),

	        // Question facultative avec un champ de commentaire (pas de choix multiples)
	        new Questions("Laissez un commentaire (facultatif).", null, false),

	        // Question sur la facilité d'utilisation de l'interface
	        new Questions("L'interface était-elle facile à utiliser ?", Arrays.asList("Oui", "Non"), true),

	        // Question sur le temps de réponse avec une note de 1 à 5
	        new Questions("Comment évaluez-vous le temps de réponse aux questions ?", Arrays.asList("1", "2", "3", "4", "5"), true),

	        // Question sur la clarté des instructions
	        new Questions("Les instructions étaient-elles claires ?", Arrays.asList("Oui", "Non"), true),

	        // Question sur la satisfaction de l'environnement
	        new Questions("L'environnement de soutenance était satisfaisant ?", Arrays.asList("Oui", "Non"), true),

	        // Question ouverte pour des suggestions
	        new Questions("Avez-vous des suggestions ?", null, false)
	    );

	    // Enregistrement des questions dans la base de données
	    questionsRepository.saveAll(questions);
        System.out.println("Questions par défaut créées !");

	}


    public Questions getQuestionById(Long id) {
        return questionsRepository.findById(id).orElse(null);
    }
	public List<Questions> getAllQuestions() {
		// TODO Auto-generated method stub
		 return questionsRepository.findAll();
		
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
