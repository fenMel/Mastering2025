package fr.esic.mastering.api;

import fr.esic.mastering.dto.*;
import fr.esic.mastering.entities.User_update;
import fr.esic.mastering.services.SoutenanceService;
import fr.esic.mastering.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final SoutenanceService soutenanceService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("Registering new user: {}", request.getUsername());
        User_update user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User_update user = userService.getUserById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> assignRole(
            @PathVariable Long id,
            @RequestBody RoleAssignmentRequest request) {
        log.info("Assigning role {} to user {}", request.getRoleName(), id);
        User_update user = userService.assignRole(id, request.getRoleName());
        return ResponseEntity.ok(convertToDTO(user));
    }

    @GetMapping("/jury")
    public ResponseEntity<List<UserDTO>> getAllJuryMembers() {
        List<UserDTO> juryMembers = userService.getUsersByRole("ROLE_JURY")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(juryMembers);
    }

    private UserDTO convertToDTO(User_update user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    @PostMapping("/jury")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createJuryMember(
            @Valid @RequestBody JuryMemberRequest request
    ) {
        log.info("Creating jury member: {}",     request.getUsername());
        User_update juryMember = soutenanceService.createJuryMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(juryMember));
    }

    // Add a method to get all coordinators
    @GetMapping("/coordinateurs")
    public ResponseEntity<List<UserDTO>> getAllCoordinateurs() {
        List<UserDTO> coordinateurs = userService.getUsersByRole("ROLE_COORDINATEUR")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(coordinateurs);
    }

    // Add a method to create a coordinator
    @PostMapping("/coordinateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createCoordinateur(
            @Valid @RequestBody CoordinateurRequest request
    ) {
        log.info("Creating coordinateur: {}", request.getUsername());
        User_update coordinateur = userService.createCoordinateur(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(coordinateur));
    }

}


