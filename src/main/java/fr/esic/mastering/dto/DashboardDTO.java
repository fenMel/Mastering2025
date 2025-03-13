package fr.esic.mastering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private List<EtudiantDTO> students; // Changed from UserDTO to EtudiantDTO
    private List<UserDTO> juryMembers;
    private List<UserDTO> coordinateurs;
    private List<SoutenanceDetailDTO> soutenances;
    private CountsDTO counts;
}

