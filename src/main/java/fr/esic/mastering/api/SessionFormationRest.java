package fr.esic.mastering.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.services.SessionFormationService;
import jakarta.validation.Valid;



    @RestController
@RequestMapping("/api/sessionsFormation")
public class SessionFormationRest {

    @Autowired
    private SessionFormationService sessionFormationService;

    // Ajouter une session

    @PostMapping
public ResponseEntity<?> createSessionFormation(@Valid @RequestBody SessionFormation sessionFormation) {
    return ResponseEntity.ok(sessionFormationService.createSessionFormation(sessionFormation));
}

    // Récupérer toutes les sessions
    @GetMapping
    public ResponseEntity<List<SessionFormation>> getAllSessions() {
        List<SessionFormation> sessions = sessionFormationService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // Mettre à jour une session
    @PutMapping("/{id}")
    public ResponseEntity<SessionFormation> updateSessionFormation(@PathVariable Long id, @RequestBody SessionFormation sessionDetails) {
        SessionFormation updatedSession = sessionFormationService.updateSessionFormation(id, sessionDetails);
        return ResponseEntity.ok(updatedSession);
    }

    // Supprimer une session
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionFormation(@PathVariable Long id) {
        sessionFormationService.deleteSessionFormation(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET /api/sessionsFormation/id/5
@GetMapping("/id/{id}")
public ResponseEntity<SessionFormation> getSessionById(@PathVariable Long id) {
    SessionFormation session = sessionFormationService.getSessionById(id);
    return ResponseEntity.ok(session);
}

// ✅ GET /api/sessionsFormation/titre/Java Avancé
@GetMapping("/titre/{titre}")
public ResponseEntity<SessionFormation> getSessionByTitre(@PathVariable String titre) {
    SessionFormation session = sessionFormationService.getSessionByTitre(titre);
    return ResponseEntity.ok(session);
}



}
