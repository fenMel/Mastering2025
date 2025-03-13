package fr.esic.mastering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevisionRequest {
    @NotBlank(message = "Les commentaires de révision sont requis")
    private String revisionComments;

    @NotNull(message = "L'ID de l'évaluateur est requis")
    private Long evaluatorId;
}