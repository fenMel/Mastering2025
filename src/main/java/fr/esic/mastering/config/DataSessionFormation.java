//package fr.esic.mastering.config;
//
//import fr.esic.mastering.entities.SessionFormation;
//import fr.esic.mastering.repository.SessionFormationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//
//@Component
//public class DataSessionFormation implements CommandLineRunner {
//
//    @Autowired
//    private SessionFormationRepository sessionFormationRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (sessionFormationRepository.count() == 0) {
//            // Création des sessions de formation
//            SessionFormation session1 = new SessionFormation(
//                null, "Session Printemps",
//                "Formation intensive sur le développement web avec Java et Spring Boot",
//                LocalDate.of(2025, 4, 1),
//                LocalDate.of(2025, 6, 30),
//                "Jean Dupont"  // Formateur
//            );
//
//            SessionFormation session2 = new SessionFormation(
//                null,
//                "Session Été",
//                "Formation avancée en cybersécurité avec mise en pratique",
//                LocalDate.of(2025, 7, 1),
//                LocalDate.of(2025, 9, 30),
//                "Marie Curie"
//            );
//
//            sessionFormationRepository.save(session1);
//            sessionFormationRepository.save(session2);
//
//            System.out.println("✅ Sessions de formation initialisées avec succès !");
//        } else {
//            System.out.println("ℹ️ Les sessions de formation existent déjà.");
//        }
//    }
//}
