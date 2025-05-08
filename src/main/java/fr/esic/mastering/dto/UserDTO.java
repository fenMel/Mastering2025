package fr.esic.mastering.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Getter
@Setter
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String lieuxDeNaissance;
    private String role;

    // Change the field type to String to match how you're using it
    private String dateNaissance;
}