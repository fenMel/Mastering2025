package fr.esic.mastering.dto;

import fr.esic.mastering.entities.MemoireMention;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoireEvaluationRequest {
    @NotNull(message = "La note est requise")
    @Min(value = 0, message = "La note doit être positive")
    @Max(value = 20, message = "La note doit être au maximum 20")
    private Double note;

    @NotNull(message = "La mention est requise")
    private MemoireMention mention;

    private String appreciation;

    @NotNull(message = "L'ID de l'évaluateur est requis")
    private Long evaluatorId;

}

