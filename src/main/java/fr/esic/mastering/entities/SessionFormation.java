package fr.esic.mastering.entities;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name = "sessions_formation")

public class SessionFormation {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
		
		 private Long id;

		// Removed duplicate 'formation' field to avoid conflict
		 
	
	 @NotBlank(message = "Le titre est obligatoire")
	    private String titre;

	    @NotBlank(message = "La description est obligatoire")
	    @Column(length = 1000)
	    private String description;

	    @NotNull(message = "La date de début est obligatoire")
	    @Future(message = "La date de début doit être dans le futur")
	    private LocalDate dateDebut;

	    @NotNull(message = "La date de fin est obligatoire")
	    @Future(message = "La date de fin doit être dans le futur")
	    private LocalDate dateFin;

	    
	
	  // ✅ Many-to-One avec Formation
	  @ManyToOne
	  @JoinColumn(name = "formation_id", nullable = false)
	  private Formation formation;
	  

    // ✅ Many-to-Many avec Etudiant
    @ManyToMany
    @JoinTable(
        name = "session_candidats",
        joinColumns = @JoinColumn(name = "session"),
        inverseJoinColumns = @JoinColumn(name = "candidat")
    )
    private List<User> users; // Liste des candidats associés à la session
	    
	  
	}







