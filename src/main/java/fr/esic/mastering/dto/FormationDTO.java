package fr.esic.mastering.dto;


import fr.esic.mastering.entities.Formation;
import lombok.Data;

@Data
public class FormationDTO {
    private Long id;
    private String nom;
    private String niveau;
    private String codeRncp;
    private String description;
    private String duree;
    private String prerequis;
    private String objectifs;

    // Convertit ce DTO en entité Formation complète
    public Formation toEntity() {
        Formation formation = new Formation();
        formation.setId(this.id);
        formation.setNom(this.nom);
        formation.setNiveau(this.niveau);
        formation.setCodeRncp(this.codeRncp);
        formation.setDescription(this.description);
        formation.setDuree(this.duree);
        formation.setPrerequis(this.prerequis);
        formation.setObjectifs(this.objectifs);
        return formation;
    }

    // Si tu souhaites aussi, on peut faire une méthode statique pour créer un DTO à partir d'une entité Formation

    public static FormationDTO fromEntity(Formation formation) {
        FormationDTO dto = new FormationDTO();
        if (formation != null) {
            dto.setId(formation.getId());
            dto.setNom(formation.getNom());
            dto.setNiveau(formation.getNiveau());
            dto.setCodeRncp(formation.getCodeRncp());
            dto.setDescription(formation.getDescription());
            dto.setDuree(formation.getDuree());
            dto.setPrerequis(formation.getPrerequis());
            dto.setObjectifs(formation.getObjectifs());
        }
        return dto;
    }
}
