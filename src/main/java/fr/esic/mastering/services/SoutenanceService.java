package fr.esic.mastering.services;

import fr.esic.mastering.dto.InscriptionRequest;
import fr.esic.mastering.dto.JuryMemberRequest;
import fr.esic.mastering.dto.SoutenanceRequest;
import fr.esic.mastering.entities.*;
import fr.esic.mastering.exceptions.InvalidOperationException;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.exceptions.SoutenanceNotFoundException;
import fr.esic.mastering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoutenanceService {
    private final SoutenanceRepository soutenanceRepository;
    private final ApprenantRepository etudiantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final User_updateRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Soutenance creerSoutenance(SoutenanceRequest request) {
        return soutenanceRepository.save(
                Soutenance.builder()
                        .dateHeure(request.getDateHeure())
                        .lieu(request.getLieu())
                        .sujet(request.getSujet())
                        .build()
        );
    }

    public Soutenance getSoutenanceById(Long soutenanceId) {
        return soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found"));
    }

    public List<Soutenance> getAllSoutenances() {
        return soutenanceRepository.findAll();
    }

    public Inscription inscrireEtudiant(Long soutenanceId, InscriptionRequest request) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new SoutenanceNotFoundException(soutenanceId));

        // Check if student exists or create new one
        Apprenants apprenant = etudiantRepository.findByEmail(request.getEmailEtudiant())
                .orElseGet(() -> {
                    // Create new student
                    Apprenants newApprenant = Apprenants.builder()
                            .email(request.getEmailEtudiant())
                            .emailVerified(false)

                            .build();
                    return etudiantRepository.save(newApprenant);
                });

        // Save inscription
        Inscription inscription = inscriptionRepository.save(
                Inscription.builder()
                        .soutenance(soutenance)
                        .etudiant(apprenant)
                        .creneauHoraire(request.getCreneauHoraire())
                        .statut(StatutInscription.EN_ATTENTE)
                        .build()
        );

        // Send verification email if student is not verified

        if (!apprenant.isEmailVerified()) {
            String token = verificationService.createVerificationToken(apprenant);  // Use the instance
            emailService.sendVerificationEmail(apprenant, token);
        }


        return inscription;
    }


    public List<Apprenants> getEtudiantsBySoutenanceId(Long soutenanceId) {
        return inscriptionRepository.findEtudiantsBySoutenanceId(soutenanceId);
    }


    @Transactional
    public Soutenance assignJury(Long soutenanceId, List<Long> juryIds) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found"));

        List<User_update> juryMembers = userRepository.findAllById(juryIds);

        // Verify all jury members exist and have JURY role
        if (juryMembers.size() != juryIds.size()) {
            throw new ResourceNotFoundException("One or more jury members not found");
        }

        // Add association between soutenance and jury members
        soutenance.setJuryMembers(juryMembers);
        Soutenance updatedSoutenance = soutenanceRepository.save(soutenance);

        // Get all students registered for this soutenance
        List<Apprenants> students = inscriptionRepository.findAllBySoutenance(soutenance)
                .stream()
                .map(Inscription::getEtudiant)
                .distinct()
                .collect(Collectors.toList());

        // Send notifications
        emailService.sendJuryAssignmentEmail(updatedSoutenance, students, juryMembers);

        return updatedSoutenance;
    }

    /**
     * Create a jury member account and send welcome email
     */
    @Transactional
    public User_update createJuryMember(JuryMemberRequest request) {
        // Generate a random password
        String password = generateRandomPassword();

        User_update juryMember = User_update.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(password))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .enabled(true)
                .roles(new HashSet<>())
                .build();

        // Get JURY role
        Role juryRole = roleRepository.findByName("ROLE_JURY")
                .orElseThrow(() -> new ResourceNotFoundException("JURY role not found"));

        juryMember.getRoles().add(juryRole);
        User_update savedJuryMember = userRepository.save(juryMember);

        // Send welcome email with credentials
        emailService.sendJuryWelcomeEmail(savedJuryMember, password);

        return savedJuryMember;
    }

    private String generateRandomPassword() {
        // Generate a random 10-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    public Soutenance assignCoordinateur(Long soutenanceId, Long coordinateurId) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found"));

        User_update coordinateur = userRepository.findById(coordinateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinateur not found"));

        // Verify user has COORDINATEUR role
        if (!coordinateur.hasRole("ROLE_COORDINATEUR")) {
            throw new InvalidOperationException("User is not a coordinateur");
        }

        soutenance.setCoordinateur(coordinateur);
        return soutenanceRepository.save(soutenance);
    }


    public Soutenance updateSoutenance(Long id, SoutenanceRequest request) {
        Soutenance existingSoutenance = soutenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Soutenance not found with id " + id));

        existingSoutenance.setSujet(request.getSujet());
        existingSoutenance.setLieu(request.getLieu());
        existingSoutenance.setDateHeure(request.getDateHeure());
        // Update other fields as necessary

        return soutenanceRepository.save(existingSoutenance);
    }
}