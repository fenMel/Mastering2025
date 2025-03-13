package fr.esic.mastering.dto;

import fr.esic.mastering.entities.DocumentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentStatusUpdateRequest {
    private DocumentStatus status;
    private String comment;
}
