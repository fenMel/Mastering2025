//package fr.esic.mastering.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import fr.esic.mastering.entities.RessourcePedagogique;
//import fr.esic.mastering.entities.SessionEntrainement;
//import fr.esic.mastering.repository.RessourcePedagogiqueRepository;
//import fr.esic.mastering.repository.SessionEntrainementRepository;
//
//import java.util.List;
//
//@Configuration
//public class RessourcePedagogiqueInitializer {
//
//    @Autowired
//    private RessourcePedagogiqueRepository ressourcePedagogiqueRepository;
//    
//    @Autowired
//    private SessionEntrainementRepository sessionEntrainementRepository;
//    
//    @Bean
//    public CommandLineRunner initRessourcesPedagogiques() {
//        return args -> {
//            // Vérifier si des ressources existent déjà
//            if (ressourcePedagogiqueRepository.count() == 0) {
//                System.out.println("Initialisation des ressources pédagogiques...");
//                
//                // Récupérer les sessions d'entraînement existantes
//                List<SessionEntrainement> sessions = sessionEntrainementRepository.findAll();
//                
//                // Si aucune session n'existe, on crée les ressources sans session
//                if (sessions.isEmpty()) {
//                    createRessourcesSansSessions();
//                } else {
//                    // Sinon, on associe les ressources aux sessions existantes
//                    createRessourcesAvecSessions(sessions);
//                }
//                
//                System.out.println("Initialisation des ressources pédagogiques terminée.");
//            } else {
//                System.out.println("Les ressources pédagogiques existent déjà.");
//            }
//        };
//    }
//    
//    /**
//     * Création de ressources pédagogiques sans les associer à des sessions
//     */
//    private void createRessourcesSansSessions() {
//        // Ressources pour Java
//        createRessource("Introduction à Java", "PDF", "/ressources/java/intro.pdf", null);
//        createRessource("Les bases de la programmation orientée objet", "PDF", "/ressources/java/poo.pdf", null);
//        createRessource("Tutoriel Java Complet", "VIDEO", "https://www.youtube.com/watch?v=java_tutorial", null);
//        createRessource("Exercices pratiques Java", "ARCHIVE", "/ressources/java/exercices.zip", null);
//        createRessource("Installation de l'environnement Java", "DOCUMENT", "/ressources/java/installation.docx", null);
//        
//        // Ressources pour Spring Boot
//        createRessource("Démarrer avec Spring Boot", "PDF", "/ressources/spring/getting-started.pdf", null);
//        createRessource("RESTful API avec Spring", "VIDEO", "https://www.youtube.com/watch?v=spring_rest_api", null);
//        createRessource("Spring Security", "PRESENTATION", "/ressources/spring/security.pptx", null);
//        createRessource("JPA et Hibernate", "PDF", "/ressources/spring/jpa-hibernate.pdf", null);
//        createRessource("Projet exemple Spring Boot", "ARCHIVE", "/ressources/spring/sample-project.zip", null);
//        
//        // Ressources pour le développement web
//        createRessource("HTML5 et CSS3", "PDF", "/ressources/web/html-css.pdf", null);
//        createRessource("Introduction à JavaScript", "PDF", "/ressources/web/javascript.pdf", null);
//        createRessource("Tutoriel React", "VIDEO", "https://www.youtube.com/watch?v=react_tutorial", null);
//        createRessource("Exercices pratiques développement web", "ARCHIVE", "/ressources/web/exercices.zip", null);
//        createRessource("Responsive Design", "DOCUMENT", "/ressources/web/responsive.docx", null);
//    }
//    
//    /**
//     * Création de ressources pédagogiques associées à des sessions existantes
//     */
//    private void createRessourcesAvecSessions(List<SessionEntrainement> sessions) {
//        if (sessions.size() > 0) {
//            SessionEntrainement session1 = sessions.get(0);
//            
//            createRessource("Introduction à Java", "PDF", "/ressources/java/intro.pdf", session1);
//            createRessource("Les bases de la programmation orientée objet", "PDF", "/ressources/java/poo.pdf", session1);
//            createRessource("Tutoriel Java Complet", "VIDEO", "https://www.youtube.com/watch?v=java_tutorial", session1);
//        }
//        
//        if (sessions.size() > 1) {
//            SessionEntrainement session2 = sessions.get(1);
//            
//            createRessource("Démarrer avec Spring Boot", "PDF", "/ressources/spring/getting-started.pdf", session2);
//            createRessource("RESTful API avec Spring", "VIDEO", "https://www.youtube.com/watch?v=spring_rest_api", session2);
//            createRessource("Spring Security", "PRESENTATION", "/ressources/spring/security.pptx", session2);
//        }
//        
//        if (sessions.size() > 2) {
//            SessionEntrainement session3 = sessions.get(2);
//            
//            createRessource("HTML5 et CSS3", "PDF", "/ressources/web/html-css.pdf", session3);
//            createRessource("Introduction à JavaScript", "PDF", "/ressources/web/javascript.pdf", session3);
//            createRessource("Tutoriel React", "VIDEO", "https://www.youtube.com/watch?v=react_tutorial", session3);
//        }
//    }
//    
//    /**
//     * Méthode utilitaire pour créer une ressource pédagogique
//     */
//    private RessourcePedagogique createRessource(String titre, String type, String chemin, SessionEntrainement session) {
//        RessourcePedagogique ressource = new RessourcePedagogique();
//        ressource.setTitre(titre);
//        ressource.setType(type);
//        ressource.setChemin(chemin);
//        ressource.setSessionEntrainement(session);
//        return ressourcePedagogiqueRepository.save(ressource);
//    }
//}