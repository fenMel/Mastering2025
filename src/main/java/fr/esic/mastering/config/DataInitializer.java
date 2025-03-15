package fr.esic.mastering.config;

import fr.esic.mastering.entities.Role_update;
import fr.esic.mastering.repository.Role_update_repository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//DAtainit pour initialiser des rôle au lancement
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(Role_update_repository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role_update(null, "ROLE_ADMIN"));
                roleRepository.save(new Role_update(null, "ROLE_JURY"));
                roleRepository.save(new Role_update(null, "ROLE_ETUDIANT"));
                roleRepository.save(new Role_update(null, "ROLE_COORDINATEUR"));
                System.out.println("Rôles initialisés.");
            }
        };
    }
}


//DataInit du fichier de départ tout le code était commenté


// package fr.esic.mastering.config;


// import fr.esic.mastering.entities.Formation;
// import fr.esic.mastering.entities.Role;
// import fr.esic.mastering.entities.User;
// import fr.esic.mastering.repository.FormationRepository;
// import fr.esic.mastering.repository.RoleRepository;
// import fr.esic.mastering.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.text.SimpleDateFormat;
// import java.util.Date;

// @Component
// public class DataInitializer implements CommandLineRunner {

//     @Autowired
//     private FormationRepository formationRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private RoleRepository roleRepository;

//     @Override
//     public void run(String... args) throws Exception {
//         // Création des rôles
//         Role adminRole = new Role();
//         adminRole.setName("Admin");

//         Role userRole = new Role();
//         userRole.setName("User");

//         roleRepository.save(adminRole);
//         roleRepository.save(userRole);

//         // Création des formations
//         Formation javaFormation = new Formation();
//         javaFormation.setNom("Développement Java");
//         javaFormation.setNiveau("Intermédiaire");

//         Formation pythonFormation = new Formation();
//         pythonFormation.setNom("Introduction à Python");
//         pythonFormation.setNiveau("Débutant");

//         formationRepository.save(javaFormation);
//         formationRepository.save(pythonFormation);

//         // Formatage des dates de naissance
//         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//         Date dateNaissanceAlice = dateFormat.parse("1990-05-20");
//         Date dateNaissanceBob = dateFormat.parse("1985-08-15");
//         Date dateNaissanceCharlie = dateFormat.parse("2000-12-01");

//         // Création des utilisateurs
//         User alice = new User();
//         alice.setNom("Alice");
//         alice.setPrenom("Dupont");
//         alice.setEmail("alice@example.com");
//         alice.setPassword("password123");
//         alice.setDateNaissance(dateNaissanceAlice);
//         alice.setTel("+33612345678");
//         alice.setLieuxDeNaissance("Paris");
//         alice.setRole(adminRole); // Rôle Admin
//         alice.setFormation(javaFormation);

//         User bob = new User();
//         bob.setNom("Bob");
//         bob.setPrenom("Martin");
//         bob.setEmail("bob@example.com");
//         bob.setPassword("password456");
//         bob.setDateNaissance(dateNaissanceBob);
//         bob.setTel("+33687654321");
//         bob.setLieuxDeNaissance("Lyon");
//         bob.setRole(userRole); // Rôle User
//         bob.setFormation(javaFormation);

//         User charlie = new User();
//         charlie.setNom("Charlie");
//         charlie.setPrenom("Durand");
//         charlie.setEmail("charlie@example.com");
//         charlie.setPassword("password789");
//         charlie.setDateNaissance(dateNaissanceCharlie);
//         charlie.setTel("+33712398765");
//         charlie.setLieuxDeNaissance("Marseille");
//         charlie.setRole(userRole); // Rôle User
//         charlie.setFormation(pythonFormation);

//         // Sauvegarde des utilisateurs
//         userRepository.save(alice);
//         userRepository.save(bob);
//         userRepository.save(charlie);

//         System.out.println("Données initialisées avec succès !");
//     }
// }