package fr.esic.mastering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountsDTO {
    private int totalStudents;
    private int totalJuryMembers;
    private int totalCoordinateurs;
    private int totalSoutenances;
    private int pendingSoutenances;
    private int completedSoutenances;
}