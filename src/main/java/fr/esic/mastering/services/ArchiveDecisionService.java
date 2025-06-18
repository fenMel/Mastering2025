package fr.esic.mastering.services;

import fr.esic.mastering.dto.ArchiveDecisionDto;
import fr.esic.mastering.entities.ArchiveDecision;
import fr.esic.mastering.repository.ArchiveDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchiveDecisionService {

    @Autowired
    private ArchiveDecisionRepository archiveDecisionRepository;

    public ArchiveDecisionDto save(ArchiveDecisionDto dto) {
        ArchiveDecision entity = new ArchiveDecision();
        entity.setId(dto.getId());
        entity.setCandidatId(dto.getCandidatId());
        entity.setCandidatNom(dto.getCandidatNom());           // NOUVEAU
        entity.setCandidatPrenom(dto.getCandidatPrenom());     // NOUVEAU
        entity.setJuryId(dto.getJuryId());
        entity.setJuryNom(dto.getJuryNom());                   // NOUVEAU
        entity.setJuryPrenom(dto.getJuryPrenom());             // NOUVEAU
        entity.setMoyenne(dto.getMoyenne());
        entity.setCommentaire(dto.getCommentaire());
        entity.setVerdict(dto.getVerdict());
        entity.setDateArchivage(dto.getDateArchivage());
        entity.setArchivePar(dto.getArchivePar());
        archiveDecisionRepository.save(entity);
        return dto;
    }

    public List<ArchiveDecisionDto> findAll() {
        return archiveDecisionRepository.findAll().stream().map(entity -> {
            ArchiveDecisionDto dto = new ArchiveDecisionDto();
            dto.setId(entity.getId());
            dto.setCandidatId(entity.getCandidatId());
            dto.setCandidatNom(entity.getCandidatNom());
            dto.setCandidatPrenom(entity.getCandidatPrenom());
            dto.setJuryId(entity.getJuryId());
            dto.setJuryNom(entity.getJuryNom());
            dto.setJuryPrenom(entity.getJuryPrenom());
            dto.setMoyenne(entity.getMoyenne());
            dto.setCommentaire(entity.getCommentaire());
            dto.setVerdict(entity.getVerdict());
            dto.setDateArchivage(entity.getDateArchivage());
            dto.setArchivePar(entity.getArchivePar());
            return dto;
        }).collect(Collectors.toList());
    }
}