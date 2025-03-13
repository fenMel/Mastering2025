package fr.esic.mastering.services;

import fr.esic.mastering.dto.ApprenantsDTO;
import fr.esic.mastering.dto.ProfilSetupRequest;
import fr.esic.mastering.entities.Apprenants;
import fr.esic.mastering.exceptions.InvalidOperationException;
import fr.esic.mastering.exceptions.ResourceNotFoundException;
import fr.esic.mastering.repository.ApprenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfilService {
    private final ApprenantRepository apprenantRepository;
    private final EmailService emailService;

    @Transactional
    public Apprenants setupProfil(ProfilSetupRequest request) {
        // Vérifier si l'email a été vérifié
        Apprenants apprenant = apprenantRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email non trouvé"));

        if (!apprenant.isEmailVerified()) {
            throw new InvalidOperationException("Veuillez d'abord vérifier votre email");
        }

        // Mettre à jour le profil
        apprenant.setNom(request.getNom());
        apprenant.setPrenom(request.getPrenom());
        apprenant.setTelephone(request.getTelephone());
        apprenant.setNiveauEtude(request.getNiveauEtude());
        apprenant.setSpecialite(request.getSpecialite());

        Apprenants updatedApprenant = apprenantRepository.save(apprenant);

        // Envoyer un email de confirmation
        emailService.sendProfilCompletedEmail(updatedApprenant);

        return updatedApprenant;
    }

    public ApprenantsDTO getProfilByEmail(String email) {
        Apprenants apprenant = apprenantRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé"));

        return convertToDTO(apprenant);
    }


    private ApprenantsDTO convertToDTO(Apprenants apprenant) {
        return ApprenantsDTO.builder()
                .id(apprenant.getId())
                .nom(apprenant.getNom())
                .prenom(apprenant.getPrenom())
                .email(apprenant.getEmail())
                .telephone(apprenant.getTelephone())
                .niveauEtude(apprenant.getNiveauEtude())
                .specialite(apprenant.getSpecialite())
                .emailVerified(apprenant.isEmailVerified())
                .status(apprenant.getStatus())
                .build();
    }
}
