package fr.esic.mastering.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esic.mastering.entities.Evaluation;
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

import java.util.List;

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
        evaluation.setCommentaire("Test intégré");
        evaluation.setNotePresentation(12);
        evaluation.setNoteContenu(14);
        evaluation.setNoteClarte(13);
        evaluation.setNotePertinence(15);
        evaluation.setNoteReponses(16);

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
        mockMvc.perform(delete("/api/evaluations/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent()); // ou .isNotFound()
    }

    @Test
    void resetEvaluation_ShouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(put("/api/evaluations/1/reset")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()); // ou .isNotFound() si 1 n'existe pas
    }
}
