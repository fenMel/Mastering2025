package fr.esic.mastering.entities;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
   private String titre;
   private String description;
   @Future
   private LocalDate dateDebut;
   @Future
   private LocalDate dateFin;
   @ManyToOne(optional = false)
@JoinColumn(name = "formation_id", nullable = false)
private Formation formation;
   @ManyToMany
   @JoinTable(
       name = "session_utilisateur",
       joinColumns = @JoinColumn(name = "session_id"),
       inverseJoinColumns = @JoinColumn(name = "user_id")
   )
   private List<User> candidats;
	}







