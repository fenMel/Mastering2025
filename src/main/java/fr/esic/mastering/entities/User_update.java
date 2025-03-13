package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User_update")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User_update {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Helper methods for role management
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isJury() {
        return hasRole("ROLE_JURY");
    }

    public boolean isEtudiant() {
        return hasRole("ROLE_ETUDIANT");
    }

    public boolean isCoordinateur() {
        return hasRole("ROLE_COORDINATEUR");
    }

    // Method to check if user has either JURY or COORDINATEUR role
    public boolean canReviewThesis() {
        return isJury() || isCoordinateur();
    }

    // Method to check if user can participate in soutenances
    public boolean canParticipateSoutenance() {
        return isJury() || isCoordinateur() || isAdmin();
    }
}