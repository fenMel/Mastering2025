package fr.esic.mastering.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.mastering.entities.SessionSoutenance;
import fr.esic.mastering.services.SessionSoutenanceService;

@RestController
@RequestMapping("/api/sessions")
public class SessionSoutenanceController {

    @Autowired
    private SessionSoutenanceService sessionSoutenanceService;

    @GetMapping("/")
    public List<SessionSoutenance> getAllSessions() {
        return sessionSoutenanceService.getAllSessions();
    }

    @PostMapping("/")
    public ResponseEntity<SessionSoutenance> createSession(@RequestBody SessionSoutenance sessionSoutenance) {
        SessionSoutenance createdSession = sessionSoutenanceService.createSession(sessionSoutenance);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionSoutenance> getSessionById(@PathVariable Long id) {
        Optional<SessionSoutenance> session = sessionSoutenanceService.getSessionById(id);
        return session.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionSoutenance> updateSession(@PathVariable Long id, @RequestBody SessionSoutenance sessionSoutenance) {
        SessionSoutenance updatedSession = sessionSoutenanceService.updateSession(id, sessionSoutenance);
        return updatedSession != null ? ResponseEntity.ok(updatedSession) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionSoutenanceService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
