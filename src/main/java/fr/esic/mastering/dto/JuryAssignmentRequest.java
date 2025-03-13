package fr.esic.mastering.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class JuryAssignmentRequest {
    @NotEmpty(message = "At least one jury member is required")
    private List<Long> juryIds;
}