package fr.esic.mastering.services;



import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAttributeService userAttributeService;


    // Show the profile completion form
    @GetMapping("/complete-profile")
    public String showCompleteProfileForm(@RequestParam String token, Model model) {
        // Find user by token
        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isEmpty() || userOpt.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token");
            return "error";
        }

        User user = userOpt.get();
        model.addAttribute("token", token);
        model.addAttribute("user", user);

        // Add role-specific information to the model
        RoleType roleType = user.getRole().getRoleUtilisateur();
        model.addAttribute("roleType", roleType);


        // Determine which form template to use based on role
        return switch (roleType) {
            case JURY -> "profile/jury-profile";
            case CANDIDAT -> "profile/candidat-profile";
            case CORDINATEUR -> "profile/coordinator-profile";
            default -> "profile/default-profile";


        };


    }

    // Process the form submission
    @PostMapping("/complete-profile")
    public String processProfileCompletion(
            @RequestParam String token,
            @RequestParam(required = false) String password,
            @RequestParam RoleType roleType,
            @ModelAttribute UserProfileForm userForm,
            Model model) {

        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isEmpty() || userOpt.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token");
            return "error";
        }

        User user = userOpt.get();

        // Update basic fields
        user.setNom(userForm.getNom());
        user.setPrenom(userForm.getPrenom());
        user.setEmail(userForm.getEmail());
        user.setTel(userForm.getTel());
        user.setLieuxDeNaissance(userForm.getLieuxDeNaissance());

        // Handle date conversion
        try {
            if (userForm.getDateNaissance() != null && !userForm.getDateNaissance().isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(userForm.getDateNaissance());
                user.setDateNaissance(date);
            }
        } catch (ParseException e) {
            // Log the error but continue with other updates
            System.err.println("Error parsing date: " + e.getMessage());
        }

        // Update password if provided
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        // Save common user data
        userRepository.save(user);

        // Save role-specific attributes
        saveRoleSpecificAttributes(user.getId(), roleType, userForm);

        // Clear the token
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        model.addAttribute("success", true);
        model.addAttribute("roleType", roleType);

        return "profile/completion-success";
    }

    private void saveRoleSpecificAttributes(Long userId, RoleType roleType, UserProfileForm userForm) {
        switch (roleType) {
            case JURY:
                // Save fields that exist in your UserProfileForm
                userAttributeService.setAttribute(userId, "specialization", userForm.getSpecialization());
                userAttributeService.setAttribute(userId, "availability", userForm.getAvailability());
                break;

            case CANDIDAT:
                // Save fields that exist in your UserProfileForm
                userAttributeService.setAttribute(userId, "cv", userForm.getCv());
                userAttributeService.setAttribute(userId, "motivationLetter", userForm.getMotivationLetter());
                break;

            case CORDINATEUR:
                // Save fields that exist in your UserProfileForm
                userAttributeService.setAttribute(userId, "department", userForm.getDepartment());
                break;
        }
    }


    /**
     * Process role-specific fields from the form
     */


    private void processRoleSpecificFields(User user, UserProfileForm userForm, RoleType roleType) {
        // Store role-specific information in appropriate fields or related tables
        // This implementation will depend on your data model
        switch (roleType) {
            case JURY:
                // For example, you might store this in user metadata or a related table
                // This is just an example - adjust based on your data model
                if (userForm.getSpecialization() != null) {
                    // Store specialization
                }
                if (userForm.getAvailability() != null) {
                    // Store availability
                }
                break;

            case CANDIDAT:
                // Store candidate-specific fields
                if (userForm.getCv() != null) {
                    // Store CV information
                }
                if (userForm.getMotivationLetter() != null) {
                    // Store motivation letter
                }
                break;

            case CORDINATEUR:
                // Store coordinator-specific fields
                if (userForm.getDepartment() != null) {
                    // Store department
                }
                break;

            default:
                // No special processing for default case
                break;
        }
    }
}