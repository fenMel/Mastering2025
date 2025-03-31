package fr.esic.mastering.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor

@AllArgsConstructor

@Entity

public class Formation {
 
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
 
    @NotBlank(message = "Le champ nom est obligatoire.")

    @Column(nullable = false)

    private String nom;
 
    @NotBlank(message = "Le champ niveau est obligatoire.")

    @Column(nullable = false)

    @NotBlank(message = "Le champ niveau est obligatoire.")
    private String niveau;
 
    private String codeRncp;
 
    @Column(length = 500)

    private String description;
 
    private String duree;
 
    private String prerequis;
 
    @Column(length = 500)

    private String objectifs;

}

 