//package fr.esic.mastering.config;
//
//import java.text.SimpleDateFormat;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import fr.esic.mastering.entities.Formation;
//import fr.esic.mastering.entities.Role;
//import fr.esic.mastering.entities.RoleType;
//import fr.esic.mastering.entities.User;
//import fr.esic.mastering.repository.FormationRepository;
//import fr.esic.mastering.repository.RoleRepository;
//import fr.esic.mastering.repository.UserRepository;
//
//@Component
//public class DataUser implements CommandLineRunner {
//
//    @Autowired
//    private FormationRepository formationRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Création des rôles
//        Role roleUtilisateur1 = new Role();
//        roleUtilisateur1.setRoleUtilisateur(RoleType.CORDINATEUR);
//        roleUtilisateur1.setName("COORDINATEUR");
//
//        Role roleUtilisateur2 = new Role();
//        roleUtilisateur2.setRoleUtilisateur(RoleType.APPRENANT);
//        roleUtilisateur2.setName("APPRENANT");
//
//        Role roleUtilisateur3 = new Role();
//        roleUtilisateur3.setRoleUtilisateur(RoleType.JURY);
//        roleUtilisateur3.setName("JURY");
//
//        // Sauvegarde des rôles dans la base de données
//        roleRepository.save(roleUtilisateur1);
//        roleRepository.save(roleUtilisateur2);
//        roleRepository.save(roleUtilisateur3);
//
//        // Création des formations
//        Formation BtsSlamFormation = new Formation();
//        BtsSlamFormation.setNom("BTS SLAM");
//        BtsSlamFormation.setNiveau("2");
//        BtsSlamFormation.setCodeRncp("bts00");
//        BtsSlamFormation.setDescription("DEVELOPPEMENT WEB");
//        BtsSlamFormation.setDuree("1 an");
//        BtsSlamFormation.setPrerequis("bac");
//        BtsSlamFormation.setObjectifs("Développer des API");
//
//        Formation BtsSisrFormation = new Formation();
//        BtsSisrFormation.setNom("BTS SISR");
//        BtsSisrFormation.setNiveau("2");
//        BtsSisrFormation.setCodeRncp("bts01");
//        BtsSisrFormation.setDescription("RESEAU INFORMATIQUE");
//        BtsSisrFormation.setDuree("1 an");
//        BtsSisrFormation.setPrerequis("bac");
//        BtsSisrFormation.setObjectifs("Résoudre des problèmes informatiques");
//
//        Formation BtsCielFormation = new Formation();
//        BtsCielFormation.setNom("BTS CIEL");
//        BtsCielFormation.setNiveau("2");
//        BtsCielFormation.setCodeRncp("bts02");
//        BtsCielFormation.setDescription("DEVELOPPEMENT INFORMATIQUE");
//        BtsCielFormation.setDuree("1 an");
//        BtsCielFormation.setPrerequis("bac");
//        BtsCielFormation.setObjectifs("Résoudre des problèmes informatiques");
//
//        formationRepository.save(BtsSlamFormation);
//        formationRepository.save(BtsSisrFormation);
//        formationRepository.save(BtsCielFormation);
//
//        // Formatage des dates de naissance
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date dateNaissanceAlice = dateFormat.parse("1990-05-20");
//        java.util.Date dateNaissanceBob = dateFormat.parse("1985-08-15");
//        java.util.Date dateNaissanceCharlie = dateFormat.parse("2000-12-01");
//
//        // Création des utilisateurs
//        User alice = new User();
//        alice.setNom("Alice");
//        alice.setPrenom("Dupont");
//        alice.setEmail("alice@example.com");
//        alice.setPassword("password123");
//        alice.setDateNaissance(dateNaissanceAlice);
//        alice.setEmail("+33612345678");
//        alice.setLieuxDeNaissance("Paris");
//        alice.setRole(roleUtilisateur1); // Rôle Admin
//        alice.setFormation(BtsSlamFormation);
//
//        User bob = new User();
//        bob.setNom("Bob");
//        bob.setPrenom("Martin");
//        bob.setEmail("bob@example.com");
//        bob.setPassword("password456");
//        bob.setDateNaissance(dateNaissanceBob);
//        bob.setEmail("+33687654321");
//        bob.setLieuxDeNaissance("Lyon");
//        bob.setRole(roleUtilisateur2); // Rôle User
//        bob.setFormation(BtsSisrFormation);
//
//        User charlie = new User();
//        charlie.setNom("Charlie");
//        charlie.setPrenom("Durand");
//        charlie.setEmail("charlie@example.com");
//        charlie.setPassword("password789");
//        charlie.setDateNaissance(dateNaissanceCharlie);
//        charlie.setEmail("+33712398765");
//        charlie.setLieuxDeNaissance("Marseille");
//        charlie.setRole(roleUtilisateur3); // Rôle User
//        charlie.setFormation(BtsCielFormation);
//
//        // Sauvegarde des utilisateurs
//        userRepository.save(alice);
//        userRepository.save(bob);
//        userRepository.save(charlie);
//
//        System.out.println("Données initialisées avec succès !");
//    }
//}
