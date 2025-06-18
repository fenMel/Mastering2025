package fr.esic.mastering.api;

import fr.esic.mastering.dto.EvaluationDTO;
import fr.esic.mastering.entities.Evaluation;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.services.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test unitaire pour le contrôleur EvaluationRest.
 * Utilise JUnit 5 et Mockito pour tester le comportement de chaque endpoint.
 */
class EvaluationRestTest {

    @Mock
    private EvaluationService evaluationService; // Service mocké

    @InjectMocks
    private EvaluationRest evaluationRest; // Contrôleur à tester

    private Evaluation testEvaluation;
    private EvaluationDTO testEvaluationDTO;
    private User testJury;

    /**
     * Prépare les objets de test avant chaque méthode de test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks

        // Jury fictif
        testJury = new User();
        testJury.setId(100L);
        testJury.setNom("Dupont");
        testJury.setPrenom("Jean");
        testJury.setEmail("jean.dupont@example.com");

        // Évaluation fictive
        testEvaluation = new Evaluation();
        testEvaluation.setId(1L);
        testEvaluation.setJury(testJury);
        testEvaluation.setCommentaire("Très bon candidat");
        testEvaluation.setNotePresentation(14);
        testEvaluation.setNoteContenu(15);
        testEvaluation.setNoteClarte(13);
        testEvaluation.setNotePertinence(16);
        testEvaluation.setNoteReponses(17);
        testEvaluation.calculerMoyenne();

        // DTO correspondant
        testEvaluationDTO = new EvaluationDTO();
        testEvaluationDTO.setId(1L);
        testEvaluationDTO.setNotePresentation(14);
        testEvaluationDTO.setNoteContenu(15);
        testEvaluationDTO.setNoteClarte(13);
        testEvaluationDTO.setNotePertinence(16);
        testEvaluationDTO.setNoteReponses(17);
        testEvaluationDTO.setMoyenne(testEvaluation.getMoyenne());
        testEvaluationDTO.setCommentaire("Très bon candidat");
    }

    /**
     * Teste la récupération de toutes les évaluations avec succès.
     */
    @Test
    void getAllEvaluations_Success() {
        when(evaluationService.getAll()).thenReturn(List.of(testEvaluation));
        when(evaluationService.convertToDTO(any())).thenReturn(testEvaluationDTO);

        ResponseEntity<?> response = evaluationRest.getAllEvaluations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List<?>);
    }

    /**
     * Teste l'ajout d'une évaluation avec succès.
     */
    @Test
    void addEvaluation_Success() {
        when(evaluationService.addEvaluation(any())).thenReturn(testEvaluation);

        ResponseEntity<?> response = evaluationRest.addEvaluation(testEvaluation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("succès"));
    }

    /**
     * Teste la récupération d'une évaluation existante par son ID.
     */
    @Test
    void getEvaluationById_Success() {
        when(evaluationService.getById(1L)).thenReturn(Optional.of(testEvaluation));

        ResponseEntity<?> response = evaluationRest.getEvaluationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Teste la récupération d'une évaluation inexistante.
     */
    @Test
    void getEvaluationById_NotFound() {
        when(evaluationService.getById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = evaluationRest.getEvaluationById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Teste la suppression d'une évaluation.
     */
    @Test
    void deleteEvaluation_Success() {
        doNothing().when(evaluationService).deleteEvaluation(1L);

        ResponseEntity<?> response = evaluationRest.deleteEvaluation(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Teste la mise à jour d'une évaluation existante.
     */
    @Test
    void updateEvaluation_Success() {
        when(evaluationService.updateEvaluation(eq(1L), any())).thenReturn(testEvaluation);

        ResponseEntity<Evaluation> response = evaluationRest.updateEvaluation(1L, testEvaluation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEvaluation, response.getBody());
    }

    /**
     * Teste la tentative de mise à jour d'une évaluation inexistante.
     */
    @Test
    void updateEvaluation_NotFound() {
        when(evaluationService.updateEvaluation(eq(999L), any())).thenReturn(null);

        ResponseEntity<Evaluation> response = evaluationRest.updateEvaluation(999L, testEvaluation);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Teste la réinitialisation d'une évaluation existante.
     */
    @Test
    void resetEvaluation_Success() {
        when(evaluationService.resetEvaluationData(1L)).thenReturn(testEvaluation);

        ResponseEntity<Evaluation> response = evaluationRest.resetEvaluation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Teste la tentative de réinitialisation d'une évaluation inexistante.
     */
    @Test
    void resetEvaluation_NotFound() {
        when(evaluationService.resetEvaluationData(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Evaluation> response = evaluationRest.resetEvaluation(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Teste la récupération des évaluations par ID de candidat.
     */
    @Test
    void getEvaluationsByCandidat() {
        when(evaluationService.getEvaluationsByCandidat(1L)).thenReturn(List.of(testEvaluation));

        ResponseEntity<?> response = evaluationRest.getEvaluationsByCandidat(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Teste la récupération des évaluations par ID de jury.
     */
    @Test
    void getEvaluationsByJury() {
        when(evaluationService.getEvaluationsByJury(100L)).thenReturn(List.of(testEvaluation));

        ResponseEntity<?> response = evaluationRest.getEvaluationsByJury(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Teste la validation d'une évaluation (marquage comme évaluée).
     */
    @Test
    void evaluerEvaluation_Success() {
        doNothing().when(evaluationService).markAsEvaluated(1L);

        ResponseEntity<Void> response = evaluationRest.evaluerEvaluation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Teste la récupération des informations d’un jury existant.
     */
    @Test
    void getJuryInfo_Success() {
        when(evaluationService.getJuryInfo(100L)).thenReturn(testJury);

        ResponseEntity<?> response = evaluationRest.getJuryInfo(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Dupont", body.get("nom"));
    }

    /**
     * Teste la récupération des informations d’un jury inexistant.
     */
    @Test
    void getJuryInfo_NotFound() {
        when(evaluationService.getJuryInfo(999L)).thenReturn(null);

        ResponseEntity<?> response = evaluationRest.getJuryInfo(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
