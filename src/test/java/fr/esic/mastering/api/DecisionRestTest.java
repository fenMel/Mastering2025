package fr.esic.mastering.api;

import fr.esic.mastering.entities.Decision;
import fr.esic.mastering.services.DecisionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Classe de test unitaire pour le contrôleur REST DecisionRest.
 * Utilise Mockito pour simuler le service DecisionService.
 */
class DecisionRestTest {

    private final DecisionService decisionService = mock(DecisionService.class); // Mock du service
    private DecisionRest decisionRest;

    /**
     * Constructeur du test : injecte manuellement le mock via réflexion dans DecisionRest.
     * (utile si @InjectMocks n’est pas utilisé)
     */
    public DecisionRestTest() {
        decisionRest = new DecisionRest();

        try {
            // Injection manuelle du mock dans le champ privé "decisionService"
            Field decisionServiceField = DecisionRest.class.getDeclaredField("decisionService");
            decisionServiceField.setAccessible(true);
            decisionServiceField.set(decisionRest, decisionService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject mock DecisionService", e);
        }
    }

    /**
     * Teste le scénario d'ajout d'une décision avec succès.
     */
    @Test
    void addDecision_ShouldReturnSuccess() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("candidatId", 1L);
        requestData.put("juryId", 2L);
        requestData.put("commentaireFinal", "Commentaire test");

        // Simule le retour du service
        when(decisionService.addDecision(1L, 2L, "Commentaire test")).thenReturn(new Decision());

        ResponseEntity<?> response = decisionRest.addDecision(requestData);

        // Vérifie que la réponse est OK et que le service a bien été appelé
        assertEquals(200, response.getStatusCodeValue());
        verify(decisionService, times(1)).addDecision(1L, 2L, "Commentaire test");
    }

    /**
     * Teste la récupération de toutes les décisions lorsque la liste est vide.
     */
    @Test
    void getAllDecisions_ShouldReturnEmptyList() {
        when(decisionService.getAllDecisions()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = decisionRest.getAllDecisions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(decisionService, times(1)).getAllDecisions();
    }

    /**
     * Teste la suppression d'une décision par ID.
     */
   /* @Test
    void deleteDecision_ShouldReturnSuccess() {
        doNothing().when(decisionService).deleteDecision(1L);

        ResponseEntity<?> response = decisionRest.deleteDecision(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(decisionService, times(1)).deleteDecision(1L);
    }*/
}
