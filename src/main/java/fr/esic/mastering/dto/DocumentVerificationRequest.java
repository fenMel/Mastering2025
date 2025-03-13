package fr.esic.mastering.dto;

import fr.esic.mastering.entities.DocumentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentVerificationRequest {
    private DocumentStatus status;
    private String comment;
    private String adminUsername;
}

