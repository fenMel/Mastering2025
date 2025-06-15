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

@SpringBootTest
@AutoConfigureMockMvc
class EvaluationRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

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
    void getAllEvaluations_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/evaluations")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void addEvaluation_ShouldReturnOk() throws Exception {
        Evaluation evaluation = new Evaluation();
        Role roleJury = new Role();
        roleJury.setRoleUtilisateur(JURY);
        fr.esic.mastering.entities.User jury = new fr.esic.mastering.entities.User(
                1L, // ID
                "Doe", // Nom
                "John", // Prenom
                new Date(), // DateNaissance
                "123456789", // Tel
                "john.doe@example.com", // Email
                "Paris", // LieuxDeNaissance
                "password123", // Password
                roleJury // Role
        );

        Role roleCandidat = new Role();
        roleCandidat.setRoleUtilisateur(CANDIDAT);
        fr.esic.mastering.entities.User candidat = new fr.esic.mastering.entities.User(
                1L, // ID
                "Statham", // Nom
                "Jason", // Prenom
                new Date(), // DateNaissance
                "123456789", // Tel
                "Jason.doe@example.com", // Email
                "America", // LieuxDeNaissance
                "password123", // Password
                roleCandidat // Role
        );
        evaluation.setCommentaire("Test intégré");
        evaluation.setJury(jury);
        evaluation.setCandidat(candidat);
        evaluation.setNotePresentation(12);
        evaluation.setNoteContenu(14);
        evaluation.setNoteClarte(13);
        evaluation.setNotePertinence(15);
        evaluation.setNoteReponses(16);
        evaluation.setDateHeure(LocalDate.now().atStartOfDay()); // Example of a required field
        evaluation.setSujet("Sujet de l'évaluation"); // Example of another required field

        mockMvc.perform(post("/api/evaluations")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluation)))
                .andExpect(status().isOk());
    }

    @Test
    void getEvaluationById_ShouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(get("/api/evaluations/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou isNotFound() si l'ID n'existe pas
    }

    @Test
    void deleteEvaluation_ShouldReturnNoContentOrNotFound() throws Exception {
        // Supprime d'abord la décision liée à l'évaluation 1 (si elle existe)
        mockMvc.perform(delete("/api/decisions/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou isNotFound()

        // Ensuite, supprime l'évaluation
        mockMvc.perform(delete("/api/evaluations/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent()); // ou isNotFound()
    }

    @Test
    void resetEvaluation_ShouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(put("/api/evaluations/1/reset")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou .isNotFound() si 1 n'existe pas
    }
}
