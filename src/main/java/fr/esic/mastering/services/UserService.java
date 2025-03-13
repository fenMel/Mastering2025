package fr.esic.mastering.services;

import fr.esic.mastering.dto.CoordinateurRequest;
import fr.esic.mastering.dto.UserRegistrationRequest;
import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.Role_update;
import fr.esic.mastering.entities.User_update;
import fr.esic.mastering.exceptions.InvalidOperationException;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final User_updateRepository userRepository;
    private final Role_update_repository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SoutenanceRepository soutenanceRepository;
    private final InscriptionRepository inscriptionRepository;


    public User_update createUser(UserRegistrationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidOperationException("Ce nom d'utilisateur est déjà pris");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidOperationException("Cet email est déjà utilisé");
        }

        // Récupérer le rôle par défaut (ROLE_ETUDIANT par exemple)
        Role_update adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Rôle admin non trouvé"));

        User_update user = User_update.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .enabled(true)
                .build();

        user.getRoles().add(adminRole);

        return userRepository.save(user);
    }

    public User_update assignRole(Long userId, String roleName) {
        User_update user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Role_update role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé"));

        user.getRoles().clear();
        user.getRoles().add(role);

        return userRepository.save(user);
    }


    public User_update getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User_update> getUsersByRole(String roleName) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(roleName)))
                .collect(Collectors.toList());
    }


    public User_update createCoordinateur(CoordinateurRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidOperationException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidOperationException("Email already exists");
        }

        // Find the COORDINATEUR role
        Role_update coordinateurRole = roleRepository.findByName("ROLE_COORDINATEUR")
                .orElseThrow(() -> new ResourceNotFoundException("Role COORDINATEUR not found"));

        // Create the user with COORDINATEUR role
        User_update coordinateur = User_update.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .build();

        coordinateur.getRoles().add(coordinateurRole);

        return userRepository.save(coordinateur);
    }
}







// Autres méthodes utiles...




