package fr.esic.mastering.api;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.mastering.dto.ConvocationRequestDTO;
import fr.esic.mastering.entities.Convocation;
import fr.esic.mastering.services.ConvocationService;
import fr.esic.mastering.services.MailService;
import fr.esic.mastering.services.PdfService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/convocations")

public class ConvocationRest {
    @Autowired
    private ConvocationService convocationService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private MailService mailService;

    @PostMapping("/creer")
    public ResponseEntity<?> creerConvocation(@RequestBody ConvocationRequestDTO dto) throws MessagingException {
        Convocation convocation = new Convocation();
        convocation.setNomEtudiant(dto.getNomEtudiant());
        convocation.setTitreProjet(dto.getTitreProjet());
        convocation.setLieu(dto.getLieu());
        convocation.setDate(LocalDate.parse(dto.getDate()));
        convocation.setHeure(LocalTime.parse(dto.getHeure()));
        convocation.setJury(String.join(", ", dto.getMembresJury()));

        Convocation saved = convocationService.createConvocation(convocation);
        File pdf = pdfService.genererConvocationPdf(saved);
        mailService.envoyerConvocation("yanatremy09@gmail.com", "Convocation Soutenance", "Votre convocation", pdf);

        return ResponseEntity.ok("Convocation envoyée et sauvegardée avec succès.");
    }
}