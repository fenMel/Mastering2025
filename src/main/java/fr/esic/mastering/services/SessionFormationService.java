package fr.esic.mastering.services;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import fr.esic.mastering.dto.SessionFormationDTO;

import fr.esic.mastering.dto.SessionFormationDetailDTO;

import fr.esic.mastering.dto.UserDTO;

import fr.esic.mastering.entities.Formation;

import fr.esic.mastering.entities.SessionFormation;

import fr.esic.mastering.entities.User;

import fr.esic.mastering.entities.RoleType;

import fr.esic.mastering.repository.FormationRepository;

import fr.esic.mastering.repository.SessionFormationRepository;

import fr.esic.mastering.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service

public class SessionFormationService {

    @Autowired

    private SessionFormationRepository sessionFormationRepository;

    @Autowired

    private FormationRepository formationRepository;

    @Autowired

    private UserRepository userRepository;

   public List<SessionFormationDetailDTO> getAllSessions() {
   return sessionFormationRepository.findAllWithFormationAndCandidats()
       .stream()
       .map(this::mapToDetailDTO)
       .collect(Collectors.toList());
}

    public SessionFormationDetailDTO getById(Long id) {

        SessionFormation session = sessionFormationRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        return mapToDetailDTO(session);

    }

    public void create(SessionFormationDTO dto) {

        SessionFormation session = new SessionFormation();

        mapFromDTO(dto, session);

        sessionFormationRepository.save(session);

    }

    public void update(Long id, SessionFormationDTO dto) {

        SessionFormation session = sessionFormationRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        mapFromDTO(dto, session);

        sessionFormationRepository.save(session);

    }

    public void delete(Long id) {

        sessionFormationRepository.deleteById(id);

    }

    // 🔁 Mapping DTO → Entity

    private void mapFromDTO(SessionFormationDTO dto, SessionFormation session) {

        session.setTitre(dto.getTitre());

        session.setDescription(dto.getDescription());

        session.setDateDebut(dto.getDateDebut());

        session.setDateFin(dto.getDateFin());

        Formation formation = formationRepository.findById(dto.getFormationId())

                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        session.setFormation(formation);

        List<User> candidats = userRepository.findAllByRoleAndIds(RoleType.CANDIDAT, dto.getCandidatIds());

        session.setCandidats(candidats);

    }

    // 🔁 Mapping Entity → DTO

    private SessionFormationDetailDTO mapToDetailDTO(SessionFormation session) {

        SessionFormationDetailDTO dto = new SessionFormationDetailDTO();

        dto.setId(session.getId());

        dto.setTitre(session.getTitre());

        dto.setDescription(session.getDescription());

        dto.setDateDebut(session.getDateDebut());

        dto.setDateFin(session.getDateFin());

        dto.setFormation(session.getFormation());

        List<UserDTO> candidatsDTO = session.getCandidats().stream()

                .map(user -> {

                    UserDTO u = new UserDTO();

                    u.setId(user.getId());

                    u.setNom(user.getNom());

                    u.setPrenom(user.getPrenom());

                    u.setEmail(user.getEmail());

                    return u;

                }).collect(Collectors.toList());

        dto.setCandidats(candidatsDTO);

        return dto;

    }


    @Transactional
public SessionFormation createSessionFormation(SessionFormationDTO dto) {
   SessionFormation session = new SessionFormation();
   session.setTitre(dto.getTitre());
   session.setDescription(dto.getDescription());
   session.setDateDebut(dto.getDateDebut());
   session.setDateFin(dto.getDateFin());
   // Associer la formation
   Formation formation = formationRepository.findById(dto.getFormationId())
           .orElseThrow(() -> new RuntimeException("Formation non trouvée"));
   session.setFormation(formation);
   // Associer les candidats
   if (dto.getCandidatIds() != null && !dto.getCandidatIds().isEmpty()) {
       List<User> candidats = userRepository.findCandidatsByRoleAndIds(RoleType.CANDIDAT, dto.getCandidatIds());
       session.setCandidats(candidats);
   }
   return sessionFormationRepository.save(session);
}
   
}
 