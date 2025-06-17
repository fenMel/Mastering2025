package fr.esic.mastering.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archive_decisions")
public class ArchiveDecision {
    @Id
    private Long id;

    @Column(name = "candidat_id")
    private Long candidatId;

    @Column(name = "jury_id")
    private Long juryId;

    private Double moyenne;

    private String commentaire;
    private String candidatNom;
    private String candidatPrenom;
    private String verdict;
    private String juryNom;
    private String juryPrenom;
    @Column(name = "date_archivage")
    private LocalDateTime dateArchivage = LocalDateTime.now();

    @Column(name = "archive_par")
    private String archivePar;


}