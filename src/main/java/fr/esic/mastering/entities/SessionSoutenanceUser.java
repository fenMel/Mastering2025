package fr.esic.mastering.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SessionSoutenanceUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_soutenance_id", nullable = false)
    private SessionSoutenance sessionSoutenance; // La session de soutenance

    @ManyToOne
    private User user; // L'utilisateur associé à cette session

    @Enumerated(EnumType.STRING)
    private RoleType role; // Le rôle de l'utilisateur dans cette session (Coordinateur, Apprenant, etc.)

    // Getters/setters...
}
