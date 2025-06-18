package fr.esic.mastering.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.VerdictDecision;
import fr.esic.mastering.repository.DecisionRepository;
import fr.esic.mastering.repository.EvaluationRepository;
import fr.esic.mastering.repository.RoleRepository;
import fr.esic.mastering.repository.UserRepository;
import fr.esic.mastering.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.esic.mastering.entities.RoleType.CANDIDAT;
import static fr.esic.mastering.entities.RoleType.JURY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de tests d'intégration pour l'API REST des décisions (/api/decisions).
 * Ces tests vérifient le bon fonctionnement global : base, sécurité JWT, sérialisation JSON, statuts HTTP.
 */
@SpringBootTest
@AutoConfigureMockMvc
class DecisionRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private RoleRepository roleRepository;

    private String jwt;

    /**
     * Avant chaque test, génère un JWT simulé avec un utilisateur ayant le rôle "CORDINATEUR".
     */
    @BeforeEach
    void setUp() {
        UserDetails userDetails = new User(
                "dede@gmail.com",
                "LucCoord123\"",
                List.of(new SimpleGrantedAuthority("CORDINATEUR"))
        );
        jwt = jwtService.generateToken(userDetails, 1L);
    }

    /**
     * Teste l'ajout d'une décision complète via l'endpoint POST /api/decisions.
     * Crée dynamiquement un candidat, un jury et une évaluation pour valider le processus complet.
     */
    @Test
    void addDecision_ShouldReturnSuccess() throws Exception {
        // 1. Crée et sauvegarde le rôle candidat
        Role roleCandidat = roleRepository.findByRoleUtilisateur(CANDIDAT)
                .orElseGet(() -> roleRepository.save(new Role(null, CANDIDAT)));

        // 2. Crée le candidat
        fr.esic.mastering.entities.User candidat = new fr.esic.mastering.entities.User();
        candidat.setNom("Candidat");
        candidat.setPrenom("Test");
        candidat.setPassword("motdepasse");
        candidat.setEmail("candidat@example.com");
        candidat.setTel("0600000000");
        candidat.setLieuxDeNaissance("Paris");
        candidat.setDateNaissance(new Date());
        candidat.setRole(roleCandidat);
        userRepository.save(candidat);

        // 3. Crée et sauvegarde le rôle jury
        Role roleJury = roleRepository.findByRoleUtilisateur(JURY)
                .orElseGet(() -> roleRepository.save(new Role(null, JURY)));

        // 4. Crée le jury
        fr.esic.mastering.entities.User jury = new fr.esic.mastering.entities.User();
        jury.setNom("Jury");
        jury.setPrenom("Test");
        jury.setPassword("motdepasse");
        jury.setEmail("jury@example.com");
        jury.setTel("0600000001");
        jury.setLieuxDeNaissance("Lyon");
        jury.setDateNaissance(new Date());
        jury.setRole(roleJury);
        userRepository.save(jury);

        // 5. Crée une évaluation
        Evaluation evaluation = new Evaluation();
        evaluation.setCandidat(candidat);
        evaluation.setJury(jury);
        evaluation.setCommentaire("Test intégré");
        evaluation.setNotePresentation(12);
        evaluation.setNoteContenu(14);
        evaluation.setNoteClarte(13);
        evaluation.setNotePertinence(15);
        evaluation.setNoteReponses(16);
        evaluationRepository.save(evaluation);

        // 6. Prépare la requête JSON
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("candidatId", candidat.getId());
        requestData.put("juryId", jury.getId());
        requestData.put("commentaireFinal", "Commentaire test");
        requestData.put("evaluationId", evaluation.getId());

        // 7. Exécute la requête POST
        mockMvc.perform(post("/api/decisions")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Décision ajoutée avec succès"));
    }

    /**
     * Vérifie que GET /api/decisions retourne une liste vide si aucune décision n'existe.
     */
    @Test
    void getAllDecisions_ShouldReturnEmptyList() throws Exception {
        decisionRepository.deleteAll(); // Vide la base avant le test

        mockMvc.perform(get("/api/decisions")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Teste la suppression d'une décision via DELETE /api/decisions/{id}.
     * Crée tous les objets nécessaires : rôles, utilisateurs, évaluation et décision.
     */
    @Test
    void deleteDecision_ShouldReturnSuccess() throws Exception {
        // 1. Rôles
        Role roleCandidat = roleRepository.findByRoleUtilisateur(CANDIDAT)
                .orElseGet(() -> roleRepository.save(new Role(null, CANDIDAT)));
        Role roleJury = roleRepository.findByRoleUtilisateur(JURY)
                .orElseGet(() -> roleRepository.save(new Role(null, JURY)));

        // 2. Candidat
        fr.esic.mastering.entities.User candidat = new fr.esic.mastering.entities.User();
        candidat.setNom("Candidat");
        candidat.setPrenom("Test");
        candidat.setPassword("motdepasse");
        candidat.setEmail("candidat2@example.com");
        candidat.setTel("0600000000");
        candidat.setLieuxDeNaissance("Paris");
        candidat.setDateNaissance(new Date());
        candidat.setRole(roleCandidat);
        userRepository.save(candidat);

        // 3. Jury
        fr.esic.mastering.entities.User jury = new fr.esic.mastering.entities.User();
        jury.setNom("Jury");
        jury.setPrenom("Test");
        jury.setPassword("motdepasse");
        jury.setEmail("jury2@example.com");
        jury.setTel("0600000001");
        jury.setLieuxDeNaissance("Lyon");
        jury.setDateNaissance(new Date());
        jury.setRole(roleJury);
        userRepository.save(jury);

        // 4. Évaluation
        Evaluation evaluation = new Evaluation();
        evaluation.setCandidat(candidat);
        evaluation.setJury(jury);
        evaluation.setCommentaire("Test intégré");
        evaluation.setNotePresentation(12);
        evaluation.setNoteContenu(14);
        evaluation.setNoteClarte(13);
        evaluation.setNotePertinence(15);
        evaluation.setNoteReponses(16);
        evaluationRepository.save(evaluation);

        // 5. Décision
        Decision decision = new Decision();
        decision.setCommentaireFinal("Test Decision");
        decision.setCandidat(candidat);
        decision.setJury(jury);
        decision.setEvaluation(evaluation);
        decision.setVerdict(VerdictDecision.ADMIS);
        decisionRepository.save(decision);

        // 6. Suppression via API
        mockMvc.perform(delete("/api/decisions/" + decision.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("Décision supprimée avec succès !"));
    }
}
