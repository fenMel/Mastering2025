package fr.esic.mastering.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateurAssignmentRequest {
    @NotNull(message = "Coordinateur ID is required")
    private Long coordinateurId;
}