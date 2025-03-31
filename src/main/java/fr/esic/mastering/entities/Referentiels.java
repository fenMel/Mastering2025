package fr.esic.mastering.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @NoArgsConstructor @AllArgsConstructor @Entity
public class Referentiels {
	
	
	
	  @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)   
	   
	   private Long id;
	   
	    @Column(nullable = false)   
	    private String nom;
	 
	    @Column(length = 500)   
	    private String description;
	 
	    @Column(length = 500)    
	    
	     private String objectifs;
	    @Column(length = 500)    
	    
	     private String criteres;
	    
	    @ManyToOne
	    @JoinColumn(name = "formation_id") // Nom de la clé étrangère en BDD
	    private Formation formation;

	
	 
	}



