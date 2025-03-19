package fr.esic.mastering.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.entities.VerdictDecision;
import fr.esic.mastering.repository.DecisionRepository;
import fr.esic.mastering.repository.FormationRepository;
import fr.esic.mastering.repository.RoleRepository;
import fr.esic.mastering.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Configuration
public class Data15 {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private FormationRepository formationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DecisionRepository decisionRepository;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Initialisation des rôles
            createRoles();
            
            // Initialisation des formations
            createFormations();
            
            // Initialisation des utilisateurs
            createUsers();
            
            // Initialisation des décisions
            initializeDecisionData();
            
            System.out.println("Initialisation des données terminée avec succès !");
        };
    }
    
    /**
     * Création des rôles
     */
    private void createRoles() {
        if (roleRepository.count() == 0) {
            System.out.println("Création des rôles...");
            
            createRole("Coordinateur", RoleType.CORDINATEUR);
            createRole("Candidat", RoleType.CANDIDATE);
            createRole("Jury", RoleType.JURY);
            createRole("Apprenant", RoleType.APPRENANT);
            createRole("Superviseur", RoleType.SUPERVISOR);
            createRole("Support Staff", RoleType.SUPPORT_STAFF);
            
            System.out.println("Création des rôles terminée");
        } else {
            System.out.println("Les rôles existent déjà");
        }
    }
    
    /**
     * Création d'un rôle
     */
    private Role createRole(String name, RoleType roleUtilisateur) {
        Role role = new Role();
        role.setName(name);
        role.setRoleUtilisateur(roleUtilisateur);
        return roleRepository.save(role);
    }
    
    /**
     * Création des formations
     */
    private void createFormations() {
        if (formationRepository.count() == 0) {
            System.out.println("Création des formations...");
            
            createFormation("Développement Java", "Formation complète en développement Java et Spring Boot", "Avancé");
            createFormation("Développement Web", "Formation en développement web front-end et back-end", "Intermédiaire");
            createFormation("DevOps", "Formation en pratiques DevOps et outils associés", "Expert");
            
            System.out.println("Création des formations terminée");
        } else {
            System.out.println("Les formations existent déjà");
        }
    }
    
    /**
     * Création d'une formation
     */
    private Formation createFormation(String nom, String description, String niveau) {
        Formation formation = new Formation();
        formation.setNom(nom);
        formation.setDescription(description);
        formation.setNiveau(niveau);
        return formationRepository.save(formation);
    }
    
    /**
     * Création des utilisateurs
     */
    private void createUsers() {
        if (userRepository.count() == 0) {
            System.out.println("Création des utilisateurs...");
            
            try {
                // Récupération des rôles
                Role roleCandidat = roleRepository.findByRoleUtilisateur(RoleType.CANDIDATE)
                        .orElseThrow(() -> new RuntimeException("Rôle CANDIDATE non trouvé"));
                
                Role roleJury = roleRepository.findByRoleUtilisateur(RoleType.JURY)
                        .orElseThrow(() -> new RuntimeException("Rôle JURY non trouvé"));
                
                // Récupération des formations
                Optional<Formation> javaFormationOpt = formationRepository.findByNom("Développement Java");
                Formation javaFormation = javaFormationOpt.isPresent() ? javaFormationOpt.get() : null;
                
                Optional<Formation> webFormationOpt = formationRepository.findByNom("Développement Web");
                Formation webFormation = webFormationOpt.isPresent() ? webFormationOpt.get() : null;
                
                Optional<Formation> devOpsFormationOpt = formationRepository.findByNom("DevOps");
                Formation devOpsFormation = devOpsFormationOpt.isPresent() ? devOpsFormationOpt.get() : null;
                
                if (javaFormation == null || webFormation == null || devOpsFormation == null) {
                    throw new RuntimeException("Une ou plusieurs formations n'ont pas été trouvées");
                }
                
                // Création des candidats
                createUser("Dubois", "Jean", "1995-06-15", "0601020304", 
                        "jean.dubois@example.com", "Paris", roleCandidat, javaFormation);
                
                createUser("Martin", "Sophie", "1997-03-22", "0602030405", 
                        "sophie.martin@example.com", "Lyon", roleCandidat, javaFormation);
                
                createUser("Bernard", "Lucas", "1994-11-08", "0603040506", 
                        "lucas.bernard@example.com", "Marseille", roleCandidat, webFormation);
                
                createUser("Petit", "Emma", "1998-07-29", "0604050607", 
                        "emma.petit@example.com", "Bordeaux", roleCandidat, webFormation);
                
                createUser("Durand", "Thomas", "1996-09-14", "0605060708", 
                        "thomas.durand@example.com", "Lille", roleCandidat, devOpsFormation);
                
                createUser("Leroy", "Camille", "1999-01-30", "0606070809", 
                        "camille.leroy@example.com", "Nantes", roleCandidat, devOpsFormation);
                
                createUser("Moreau", "Antoine", "1993-05-17", "0607080910", 
                        "antoine.moreau@example.com", "Strasbourg", roleCandidat, javaFormation);
                
                createUser("Simon", "Julie", "1997-12-03", "0608091011", 
                        "julie.simon@example.com", "Toulouse", roleCandidat, webFormation);
                
                createUser("Michel", "Nicolas", "1995-08-25", "0609101112", 
                        "nicolas.michel@example.com", "Rennes", roleCandidat, devOpsFormation);
                
                createUser("Lefebvre", "Manon", "1998-04-18", "0610111213", 
                        "manon.lefebvre@example.com", "Nice", roleCandidat, javaFormation);
                
                // Création des jurys
                createUser("Garcia", "Philippe", "1980-02-10", "0701020304", 
                        "philippe.garcia@example.com", "Paris", roleJury, null);
                
                createUser("Roux", "Isabelle", "1985-07-22", "0702030405", 
                        "isabelle.roux@example.com", "Lyon", roleJury, null);
                
                System.out.println("Création des utilisateurs terminée");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création des utilisateurs: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Les utilisateurs existent déjà");
        }
    }
    
    /**
     * Création d'un utilisateur
     */
    private User createUser(String nom, String prenom, String dateNaissance, String tel, 
                            String email, String lieuNaissance, Role role, Formation formation) throws Exception {
        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setDateNaissance(dateFormat.parse(dateNaissance));
        user.setTel(tel);
        user.setEmail(email);
        user.setLieuxDeNaissance(lieuNaissance);
        user.setPassword("$2a$10$abcdefghijklmnopqrstuvwxyz123456789"); // Mot de passe encodé
        user.setRole(role);
        user.setFormation(formation);
        return userRepository.save(user);
    }
    
    /**
     * Initialisation des décisions
     */
    public void initializeDecisionData() {
        if (decisionRepository.count() == 0) {
            System.out.println("Création des décisions...");
            
            try {
                // Tableau des données à insérer : candidatId, juryId, commentaire, verdict
                Object[][] decisionsData = {
                    {1L, 11L, "Excellente maîtrise des concepts Java et Spring Boot. Projet très bien structuré.", VerdictDecision.ADMIS},
                    {2L, 11L, "Connaissances techniques solides mais projet incomplet. Session de rattrapage recommandée.", VerdictDecision.RATTRAPAGE},
                    {3L, 12L, "Difficultés importantes sur les concepts fondamentaux. Formation complémentaire nécessaire.", VerdictDecision.NON_ADMIS},
                    {4L, 12L, "Très bonne présentation, projet fonctionnel avec quelques améliorations possibles.", VerdictDecision.ADMIS},
                    {5L, 11L, "Bonnes connaissances théoriques mais implémentation pratique insuffisante.", VerdictDecision.RATTRAPAGE},
                    {6L, 12L, "Compétences techniques exceptionnelles, excellente méthodologie et documentation.", VerdictDecision.ADMIS},
                    {7L, 11L, "Manque de rigueur dans l'implémentation et lacunes sur les fondamentaux.", VerdictDecision.NON_ADMIS},
                    {8L, 12L, "Projet ambitieux mais non abouti. Des compétences sont présentes mais besoin de consolidation.", VerdictDecision.RATTRAPAGE},
                    {9L, 11L, "Bonne compréhension technique mais difficultés d'expression et de formalisation.", VerdictDecision.RATTRAPAGE},
                    {10L, 12L, "Excellente maîtrise technique et très bonne présentation du projet.", VerdictDecision.ADMIS}
                };
                
                // Parcours du tableau et création des décisions
                for (Object[] data : decisionsData) {
                    Long candidatId = (Long) data[0];
                    Long juryId = (Long) data[1];
                    String commentaire = (String) data[2];
                    VerdictDecision verdict = (VerdictDecision) data[3];
                    
                    addDecisionWithVerdict(candidatId, juryId, commentaire, verdict);
                }
                
                System.out.println("Création des décisions terminée");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création des décisions: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Les décisions existent déjà");
        }
    }
    
    /**
     * Méthode auxiliaire pour ajouter une décision avec un verdict spécifié
     */
    public Decision addDecisionWithVerdict(Long candidatId, Long juryId, String commentaireFinal, VerdictDecision verdict) {
        // Récupération des entités User
        User candidat = userRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec l'ID: " + candidatId));
        
        User jury = null;
        if (juryId != null) {
            jury = userRepository.findById(juryId)
                    .orElseThrow(() -> new RuntimeException("Jury non trouvé avec l'ID: " + juryId));
        }
        
        // Création de la décision
        Decision decision = new Decision();
        decision.setCandidat(candidat);
        decision.setJury(jury);
        decision.setCommentaireFinal(commentaireFinal);
        decision.setVerdict(verdict);
        
        // Sauvegarde et retour
        return decisionRepository.save(decision);
    }
}