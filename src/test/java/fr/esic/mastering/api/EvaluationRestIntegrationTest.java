package fr.esic.mastering.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.Role;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static fr.esic.mastering.entities.RoleType.CANDIDAT;
import static fr.esic.mastering.entities.RoleType.JURY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration de l'API REST /api/evaluations avec SpringBootTest et MockMvc.
 * Vérifie les réponses HTTP réelles, la sécurité JWT, et la sérialisation JSON.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EvaluationRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Permet d'appeler les endpoints REST comme si on était un client HTTP

    @Autowired
    private ObjectMapper objectMapper; // Sert à convertir les objets Java ↔ JSON

    @Autowired
    private JwtService jwtService; // Utilisé pour générer un token JWT valide

    private String jwt;

    /**
     * Génère un token JWT avant chaque test avec un utilisateur fictif ayant le rôle "CORDINATEUR".
     */
    @BeforeEach
    void setUp() {
        UserDetails userDetails = new User(
                "dede@gmail.com",
                "LucCoord123\"",
                List.of(new SimpleGrantedAuthority("CORDINATEUR"))
        );

        // Génère un token JWT simulé pour l'utilisateur
        jwt = jwtService.generateToken(userDetails, 1L);
    }

    /**
     * Vérifie que l’endpoint GET /api/evaluations retourne un statut 200 OK avec du JSON.
     */
    @Test
    void getAllEvaluations_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/evaluations")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /**
     * Vérifie que l’ajout d’une évaluation via POST /api/evaluations fonctionne correctement.
     */
    @Test
    void addEvaluation_ShouldReturnOk() throws Exception {
        Evaluation evaluation = new Evaluation();

        // Création d’un utilisateur jury fictif
        Role roleJury = new Role();
        roleJury.setRoleUtilisateur(JURY);
        fr.esic.mastering.entities.User jury = new fr.esic.mastering.entities.User(
                1L, "Doe", "John", new Date(), "123456789", "john.doe@example.com",
                "Paris", "password123", roleJury
        );

        // Création d’un utilisateur candidat fictif
        Role roleCandidat = new Role();
        roleCandidat.setRoleUtilisateur(CANDIDAT);
        fr.esic.mastering.entities.User candidat = new fr.esic.mastering.entities.User(
                1L, "Statham", "Jason", new Date(), "123456789", "Jason.doe@example.com",
                "America", "password123", roleCandidat
        );

        // Données de l’évaluation
        evaluation.setCommentaire("Test intégré");
        evaluation.setJury(jury);
        evaluation.setCandidat(candidat);
        evaluation.setNotePresentation(12);
        evaluation.setNoteContenu(14);
        evaluation.setNoteClarte(13);
        evaluation.setNotePertinence(15);
        evaluation.setNoteReponses(16);
        evaluation.setDateHeure(LocalDate.now().atStartOfDay());
        evaluation.setSujet("Sujet de l'évaluation");

        mockMvc.perform(post("/api/evaluations")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluation)))
                .andExpect(status().isOk());
    }

    /**
     * Vérifie la récupération d’une évaluation existante via GET /api/evaluations/{id}.
     */
    @Test
    void getEvaluationById_ShouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(get("/api/evaluations/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou isNotFound() si 1 n'existe pas
    }

    /**
     * Vérifie la suppression d’une évaluation et de sa décision associée si elle existe.
     */
    @Test
    void deleteEvaluation_ShouldReturnNoContentOrNotFound() throws Exception {
        // Tente de supprimer la décision liée à l’évaluation (si présente)
        mockMvc.perform(delete("/api/decisions/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou isNotFound()

        // Supprime ensuite l’évaluation
        mockMvc.perform(delete("/api/evaluations/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent()); // ou isNotFound()
    }

    /**
     * Vérifie que la réinitialisation d’une évaluation fonctionne via PUT /api/evaluations/{id}/reset.
     */
    @Test
    void resetEvaluation_ShouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(put("/api/evaluations/1/reset")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou .isNotFound() si 1 n'existe pas
    }
}
