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

class DecisionRestTest {

    private final DecisionService decisionService = mock(DecisionService.class);
    private DecisionRest decisionRest;

    public DecisionRestTest() {
        decisionRest = new DecisionRest();

        try {
            Field decisionServiceField = DecisionRest.class.getDeclaredField("decisionService");
            decisionServiceField.setAccessible(true);
            decisionServiceField.set(decisionRest, decisionService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to inject mock DecisionService", e);
        }
    }

    @Test
    void addDecision_ShouldReturnSuccess() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("candidatId", 1L);
        requestData.put("juryId", 2L);
        requestData.put("commentaireFinal", "Commentaire test");

        when(decisionService.addDecision(1L, 2L, "Commentaire test")).thenReturn(new Decision());

        ResponseEntity<?> response = decisionRest.addDecision(requestData);

        assertEquals(200, response.getStatusCodeValue());
        verify(decisionService, times(1)).addDecision(1L, 2L, "Commentaire test");
    }

    @Test
    void getAllDecisions_ShouldReturnEmptyList() {
        when(decisionService.getAllDecisions()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = decisionRest.getAllDecisions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(decisionService, times(1)).getAllDecisions();
    }

    @Test
    void deleteDecision_ShouldReturnSuccess() {
        doNothing().when(decisionService).deleteDecision(1L);

        ResponseEntity<?> response = decisionRest.deleteDecision(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(decisionService, times(1)).deleteDecision(1L);
    }
}