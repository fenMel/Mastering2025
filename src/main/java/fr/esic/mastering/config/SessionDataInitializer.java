package fr.esic.mastering.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

@Component
public class SessionDataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SessionDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initialisation des données de sessions d'entraînement...");
        
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Désactiver temporairement les contraintes de clés étrangères
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
            
            // Insérer les données de sessions d'entraînement
            stmt.execute("INSERT INTO session_entrainement (id, titre, description, date_debut, date_fin, coordinateur_id) VALUES " +
                    "(1, 'Atelier Java Avancé', 'Session intensive sur les concepts avancés de Java et Spring Boot', " +
                    "DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 9 DAY), 1), " +
                    "(2, 'Initiation à Python', 'Découverte des bases de Python et de ses frameworks', " +
                    "DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), 1), " +
                    "(3, 'API REST avec Spring', 'Développement et sécurisation d''API REST avec Spring Boot', " +
                    "DATE_ADD(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 23 DAY), 1), " +
                    "(4, 'DevOps et CI/CD', 'Introduction aux pratiques DevOps et à l''intégration continue', " +
                    "DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_ADD(NOW(), INTERVAL 32 DAY), 1) " +
                    "ON DUPLICATE KEY UPDATE titre=VALUES(titre)");
            
            // Insérer les relations participants-sessions
            stmt.execute("INSERT INTO session_entrainement_participants (session_entrainement_id, participants_id) VALUES " +
                    "(1, 2), " +
                    "(1, 3), " +
                    "(2, 3), " +
                    "(3, 2), " +
                    "(3, 3), " +
                    "(4, 2) " +
                    "ON DUPLICATE KEY UPDATE session_entrainement_id=VALUES(session_entrainement_id)");
            
            // Insérer les ressources pédagogiques
            stmt.execute("INSERT INTO ressource_pedagogique (id, titre, description, lien, date_publication, session_entrainement_id) VALUES " +
                    "(1, 'Guide Spring Boot', 'Documentation complète sur Spring Boot', " +
                    "'https://example.com/spring-boot-guide', NOW(), 1), " +
                    "(2, 'Exercices JPA', 'Série d''exercices sur JPA et Hibernate', " +
                    "'https://example.com/jpa-exercises', NOW(), 1), " +
                    "(3, 'Introduction à Django', 'Tutorial complet pour débutants sur Django', " +
                    "'https://example.com/django-intro', NOW(), 2), " +
                    "(4, 'Sécuriser une API Spring', 'Guide sur Spring Security et JWT', " +
                    "'https://example.com/spring-security', NOW(), 3), " +
                    "(5, 'Configuration CI avec Jenkins', 'Tutoriel sur Jenkins', " +
                    "'https://example.com/jenkins-tutorial', NOW(), 4), " +
                    "(6, 'Bonnes pratiques REST', 'Conception d''API REST selon les standards', " +
                    "'https://example.com/rest-best-practices', NOW(), 3) " +
                    "ON DUPLICATE KEY UPDATE titre=VALUES(titre)");
            
            // Insérer les annonces
            stmt.execute("INSERT INTO annonce (id, titre, contenu, date_publication, session_entrainement_id) VALUES " +
                    "(1, 'Bienvenue à l''atelier Java', " +
                    "'Merci de vous être inscrits à cet atelier. N''oubliez pas d''installer l''environnement de développement avant la séance.', " +
                    "NOW(), 1), " +
                    "(2, 'Rappel: documents à préparer', " +
                    "'Veuillez préparer les documents précisés dans le mail pour la première séance.', " +
                    "DATE_ADD(NOW(), INTERVAL 2 DAY), 1), " +
                    "(3, 'Préparation à l''atelier Python', " +
                    "'Voici la liste des prérequis pour la formation Python qui débutera prochainement.', " +
                    "DATE_ADD(NOW(), INTERVAL 1 DAY), 2), " +
                    "(4, 'Important: serveurs de test API', " +
                    "'Les informations de connexion aux serveurs de test ont été mises à jour. Veuillez consulter votre espace.', " +
                    "DATE_ADD(NOW(), INTERVAL 15 DAY), 3), " +
                    "(5, 'Accès au pipeline Jenkins', " +
                    "'Les accès à l''environnement Jenkins pour la formation DevOps sont maintenant disponibles.', " +
                    "DATE_ADD(NOW(), INTERVAL 25 DAY), 4), " +
                    "(6, 'Prérequis pour la formation API', " +
                    "'Assurez-vous d''avoir installé Postman et un JDK récent pour la formation API REST.', " +
                    "DATE_ADD(NOW(), INTERVAL 18 DAY), 3) " +
                    "ON DUPLICATE KEY UPDATE titre=VALUES(titre)");
            
            // Réactiver les contraintes de clés étrangères
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
            
            System.out.println("Données des sessions d'entraînement insérées avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'insertion des données de sessions : " + e.getMessage());
            e.printStackTrace();
        }
    }
}