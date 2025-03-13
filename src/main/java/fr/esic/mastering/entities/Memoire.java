package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Apprenants etudiant;

    @ManyToOne
    @JoinColumn(name = "soutenance_id", nullable = false)
    private Soutenance soutenance;

    private String title;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private LocalDateTime uploadDate;

    // Nouvelles colonnes pour l'évaluation
    private Double note;

    @Enumerated(EnumType.STRING)
    private MemoireMention mention;

    private String appreciation;
    private String revisionComments;

    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private User_update evaluator;

    private LocalDateTime evaluationDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemoireStatus status = MemoireStatus.SUBMITTED;
}


