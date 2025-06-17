package fr.esic.mastering.api;

import java.util.List;
import java.util.Optional;

import fr.esic.mastering.dto.SessionSoutenanceDTO;
import fr.esic.mastering.dto.SessionSoutenanceUserDTO;
import fr.esic.mastering.entities.SessionSoutenanceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    public List<SessionSoutenance> getAllSessions() {
        return sessionSoutenanceService.getAllSessions();
    }

//    @PostMapping("/")
//    public ResponseEntity<SessionSoutenance> createSession(@RequestBody SessionSoutenance sessionSoutenance) {
//        SessionSoutenance createdSession = sessionSoutenanceService.createSession(sessionSoutenance);
//        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
//    }


    // Endpoint pour créer une session de soutenance
    @PreAuthorize("hasRole('ROLE_COORDINATEUR')")
    @PostMapping
    public ResponseEntity<SessionSoutenance> createSessionSoutenance(@RequestBody SessionSoutenanceDTO sessionSoutenanceDTO) {
        try {
            // Appel au service pour créer la session de soutenance
            SessionSoutenance sessionSoutenance = sessionSoutenanceService.createSessionSoutenance(sessionSoutenanceDTO);
            return new ResponseEntity<>(sessionSoutenance, HttpStatus.CREATED);  // Retourne la session créée avec statut 201
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // Retourne une erreur 400 si une exception est levée
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionSoutenance> getSessionById(@PathVariable Long id) {
        Optional<SessionSoutenance> session = sessionSoutenanceService.getSessionById(id);
        return session.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Ajouter un utilisateur à une session de soutenance
    @PostMapping("/addUser/")
    public ResponseEntity<SessionSoutenanceUser> addUserToSession(
            @RequestBody SessionSoutenanceUserDTO sessionSoutenanceUserDTO) {

        try {
            // Appeler le service pour ajouter l'utilisateur
            SessionSoutenanceUser sessionSoutenanceUser = sessionSoutenanceService.addUserToSession(
                    sessionSoutenanceUserDTO.getSessionId(),
                    sessionSoutenanceUserDTO.getUserId(),
                    sessionSoutenanceUserDTO.getRole()
            );
            return new ResponseEntity<>(sessionSoutenanceUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



    // Mettre à jour une session de soutenance par ID
    @PutMapping("/update/{id}")
    public ResponseEntity<SessionSoutenance> updateSession(
            @PathVariable Long id, @RequestBody SessionSoutenance sessionSoutenance) {
        SessionSoutenance updatedSession = sessionSoutenanceService.updateSession(id, sessionSoutenance);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);
    }



    // Supprimer une session de soutenance par ID
    @PreAuthorize("hasRole('ROLE_COORDINATEUR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionSoutenanceService.deleteSession(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
