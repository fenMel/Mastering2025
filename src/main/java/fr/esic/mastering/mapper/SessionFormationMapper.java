package fr.esic.mastering.mapper;

import fr.esic.mastering.dto.SessionFormationDTO;
import fr.esic.mastering.entities.SessionFormation;

public class SessionFormationMapper {

    public static SessionFormationDTO toDTO(SessionFormation entity) {
        if (entity == null) return null;
        SessionFormationDTO dto = new SessionFormationDTO();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDescription(entity.getDescription());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        // autres champs si besoin
        return dto;
    }

    public static SessionFormation toEntity(SessionFormationDTO dto) {
        if (dto == null) return null;
        SessionFormation entity = new SessionFormation();
        entity.setId(dto.getId());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        // autres champs si besoin
        return entity;
    }
}

