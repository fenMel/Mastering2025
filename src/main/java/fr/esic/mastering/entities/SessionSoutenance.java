package fr.esic.mastering.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions_soutenance")

public class SessionSoutenance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  Lien avec la session de formation sélectionnée
    //@JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "session_formation_id", nullable = false)
    private SessionFormation sessionFormation;

    

    //  Date de début de la soutenance
    private LocalDate dateDebutSoutenance;

    //  Responsable (texte libre, ce n’est pas un utilisateur)
    private String responsable;

    //  Commentaire facultatif sur la date de début
    private String commentaireDateDebut;

    // Liste des utilisateurs (coordinateurs) liés à cette session
    //@JsonIgnore 
    @OneToMany(mappedBy = "sessionSoutenance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SessionSoutenanceUser> participants = new ArrayList<>();

    //  Historique de modifications (optionnel mais utile)
    // @OneToMany(mappedBy = "sessionSoutenance", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Modification> historiqueModifications = new ArrayList<>();

    //  Suivi
    private LocalDateTime dateCreation;

    private LocalDateTime dateDerniereModification;

    // @PrePersist
    // public void prePersist() {
    //     this.dateCreation = LocalDateTime.now();
    //     this.dateDerniereModification = LocalDateTime.now();
    // }

    // @PreUpdate
    // public void preUpdate() {
    //     this.dateDerniereModification = LocalDateTime.now();
    // }

    
}

