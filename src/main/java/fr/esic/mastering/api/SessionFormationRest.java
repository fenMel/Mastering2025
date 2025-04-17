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



    @RestController
@RequestMapping("/api/sessions")
public class SessionFormationRest {

    @Autowired
    private SessionFormationService sessionFormationService;

    // Ajouter une session
    @PostMapping
    public ResponseEntity<SessionFormation> createSession(@RequestBody SessionFormation session) {
        SessionFormation createdSession = sessionFormationService.createSession(session);
        return ResponseEntity.ok(createdSession);
    }

    // Récupérer toutes les sessions
    @GetMapping
    public ResponseEntity<List<SessionFormation>> getAllSessions() {
        List<SessionFormation> sessions = sessionFormationService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // Récupérer une session par ID
    @GetMapping("/{id}")
    public ResponseEntity<SessionFormation> getSessionById(@PathVariable Long id) {
        SessionFormation session = sessionFormationService.getSessionById(id);
        return ResponseEntity.ok(session);
    }
    // Récupérer une session par Titre
    @GetMapping("/{titre}")
    public ResponseEntity<SessionFormation> getSessionByTitre(@PathVariable String titre) {
        SessionFormation session = sessionFormationService.getSessionByTitre(titre);
        return ResponseEntity.ok(session);
    }

    // Mettre à jour une session
    @PutMapping("/{id}")
    public ResponseEntity<SessionFormation> updateSession(@PathVariable Long id, @RequestBody SessionFormation sessionDetails) {
        SessionFormation updatedSession = sessionFormationService.updateSession(id, sessionDetails);
        return ResponseEntity.ok(updatedSession);
    }

    // Supprimer une session
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionFormationService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
