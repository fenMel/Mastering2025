package fr.esic.mastering.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.esic.mastering.dto.SessionFormationDTO;

import fr.esic.mastering.dto.SessionFormationDetailDTO;

import fr.esic.mastering.dto.UserDTO;
import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.services.SessionFormationService;

import fr.esic.mastering.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin("*")

@RestController

@RequestMapping("/api/sessionsFormation")

public class SessionFormationRest {

    @Autowired

    private SessionFormationService sessionFormationService;

    @Autowired

    private UserService userService;

  

    @GetMapping

    public List<SessionFormationDetailDTO> getAllSessions() {

        return sessionFormationService.getAllSessions();

    }


    @GetMapping("/{id}")

    public SessionFormationDetailDTO getSessionById(@PathVariable Long id) {

        return sessionFormationService.getById(id);

    }

    

    // @PostMapping

    // public void createSession(@Valid @RequestBody SessionFormationDTO dto) {

    //     sessionFormationService.create(dto);

    // }

   

    @PutMapping("/{id}")

    public void updateSession(@PathVariable Long id, @Valid @RequestBody SessionFormationDTO dto) {

        sessionFormationService.update(id, dto);

    }

    

    @DeleteMapping("/{id}")

    public void deleteSession(@PathVariable Long id) {

        sessionFormationService.delete(id);

    }

   

    @GetMapping("/candidats")

    public List<UserDTO> getAllCandidats() {

        return userService.getAllCandidats();

    }

    @PostMapping
public ResponseEntity<SessionFormation> createSession(@RequestBody SessionFormationDTO dto) {
   SessionFormation created = sessionFormationService.createSessionFormation(dto);
   return ResponseEntity.ok(created);
}


}
 