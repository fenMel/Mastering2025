package fr.esic.mastering.api;

import fr.esic.mastering.dto.ApprenantDetailDTO;
import fr.esic.mastering.dto.ApprenantRequest;
import fr.esic.mastering.entities.Apprenants;
import fr.esic.mastering.services.ApprenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/apprenants")
@PreAuthorize("hasAnyRole('ADMIN', 'COORDINATEUR')")
@RequiredArgsConstructor
public class ApprenantController {

    private final ApprenantService apprenantService;

    @PostMapping
    public ResponseEntity<Apprenants> creerEtudiant(@Valid @RequestBody ApprenantRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apprenantService.creerEtudiant(dto));
    }

    @GetMapping
    public ResponseEntity<List<ApprenantDetailDTO>> getAllEtudiants() {
        return ResponseEntity.ok(apprenantService.getAllEtudiantsWithDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprenantDetailDTO> getEtudiantDetails(@PathVariable Long id) {
        return ResponseEntity.ok(apprenantService.getEtudiantDetails(id));
    }

}

