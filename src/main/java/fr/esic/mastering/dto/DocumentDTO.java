package fr.esic.mastering.dto;

import fr.esic.mastering.entities.DocumentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentDTO {
    private Long id;
    private String documentType;
    private String fileName;
    private String fileType;
    private DocumentStatus status;
    private LocalDateTime uploadDate;
}