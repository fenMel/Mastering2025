package fr.esic.mastering.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {
	
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY) 
 private Long id;
 
 private String nom;
 private String prenom;
 private Date DateNaissance;
 private String tel;
 
 @Column(unique = true, nullable = false)
 private String email;
 private String lieuxDeNaissance;
 
 @NotBlank(message = "Le mot de passe est obligatoire.")
    private String password;
//   samy
@ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
     @ManyToOne
   
     @JoinColumn(name = "formation_id")
    @JsonBackReference
    private Formation formation;

}
