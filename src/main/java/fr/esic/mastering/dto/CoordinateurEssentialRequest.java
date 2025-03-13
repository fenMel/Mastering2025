package fr.esic.mastering.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordinateurEssentialRequest {


    private String username;


    private String nom;

    private String prenom;


    private String email;
}
