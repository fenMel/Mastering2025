package fr.esic.mastering.dto;

import fr.esic.mastering.entities.MemoireMention;
import fr.esic.mastering.entities.MemoireStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoireDTO {
    private Long id;
    private String title;
    private String fileName;
    private String fileType;
    private long fileSize;
    private LocalDateTime uploadDate;
    private MemoireStatus status;

    // Evaluation fields
    private Double note;
    private MemoireMention mention;
    private String appreciation;
    private String revisionComments;
    private String evaluatorName;
    private LocalDateTime evaluationDate;

    // Soutenance info
    private Long soutenanceId;
    private String soutenanceSujet;
    private LocalDateTime soutenanceDate;
}