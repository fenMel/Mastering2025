package fr.esic.mastering.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ArchiveDecisionDto {
    private Long id;
    private Long candidatId;
    private Long juryId;
    private double moyenne;
    private String commentaire;
    private String verdict;
    private LocalDateTime dateArchivage;
    private String archivePar;
    private String juryNom;
    private String juryPrenom;
    private String candidatNom;
    private String candidatPrenom;

}