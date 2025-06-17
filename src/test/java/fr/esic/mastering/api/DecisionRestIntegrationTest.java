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

    @BeforeEach
    void setUp() {
        // Crée un utilisateur fictif avec autorité CORDINATEUR
        UserDetails userDetails = new User(
                "dede@gmail.com",
                "LucCoord123\"",
                List.of(new SimpleGrantedAuthority("CORDINATEUR"))
        );
        // Génère un token JWT valide
        jwt = jwtService.generateToken(userDetails, 1L); // tu peux mettre n'importe quel ID fictif
    }

    @Test
    void addDecision_ShouldReturnSuccess() throws Exception {
        // 1. Crée et sauvegarde le rôle candidat
        Role roleCandidat = roleRepository.findByRoleUtilisateur(CANDIDAT)
                .orElseGet(() -> roleRepository.save(new Role(null, CANDIDAT)));

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

        // 2. Crée et sauvegarde le rôle jury
        Role roleJury = roleRepository.findByRoleUtilisateur(JURY)
                .orElseGet(() -> roleRepository.save(new Role(null, JURY)));

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

        // 3. Crée et sauvegarde l'évaluation
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

        // 4. Appelle l'API comme avant
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("candidatId", candidat.getId());
        requestData.put("juryId", jury.getId());
        requestData.put("commentaireFinal", "Commentaire test");
        requestData.put("evaluationId", evaluation.getId());

        mockMvc.perform(post("/api/decisions")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Décision ajoutée avec succès"));
    }

    @Test
    void getAllDecisions_ShouldReturnEmptyList() throws Exception {
        decisionRepository.deleteAll(); // Nettoie la table avant le test
        mockMvc.perform(get("/api/decisions")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deleteDecision_ShouldReturnSuccess() throws Exception {
        // 1. Crée et sauvegarde les rôles
        Role roleCandidat = roleRepository.findByRoleUtilisateur(CANDIDAT)
                .orElseGet(() -> roleRepository.save(new Role(null, CANDIDAT)));
        Role roleJury = roleRepository.findByRoleUtilisateur(JURY)
                .orElseGet(() -> roleRepository.save(new Role(null, JURY)));

        // 2. Crée et sauvegarde le candidat
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

        // 3. Crée et sauvegarde le jury
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

        // 4. Crée et sauvegarde l'évaluation
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

        // 5. Crée et sauvegarde la décision avec tous les champs obligatoires
        Decision decision = new Decision();
        decision.setCommentaireFinal("Test Decision");
        decision.setCandidat(candidat);
        decision.setJury(jury);
        decision.setEvaluation(evaluation);
        decision.setVerdict(VerdictDecision.ADMIS); // ou NON_ADMIS selon ton enum
        decisionRepository.save(decision);

        mockMvc.perform(delete("/api/decisions/" + decision.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("Décision supprimée avec succès !"));
    }
}