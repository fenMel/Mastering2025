package fr.esic.mastering.dto;

import java.time.LocalDate;
import java.util.List;

import fr.esic.mastering.entities.Formation;
import lombok.Data;

@Data
public class SessionFormationDetailDTO {
   private Long id;
   private String titre;
   private String description;
   private LocalDate dateDebut;
   private LocalDate dateFin;
   private Formation formation;
   private List<UserDTO> candidats;
}
