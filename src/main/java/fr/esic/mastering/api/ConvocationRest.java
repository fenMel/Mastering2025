package fr.esic.mastering.api;

import com.itextpdf.text.DocumentException;
import fr.esic.mastering.services.EmailTemplateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import fr.esic.mastering.dto.ConvocationDTO;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.UserRepository;
import fr.esic.mastering.services.EmailService;
import fr.esic.mastering.services.PdfGenerationService;

@RestController
@RequestMapping("/users/send")
@CrossOrigin(origins = "*")
public class ConvocationRest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfGenerationService pdfService;

    @Autowired
    private EmailService emailService;

    @PostMapping("mail_convocation")
    public ResponseEntity<?> sendConvocation(@RequestBody ConvocationDTO convocationDTO) {
        System.out.println("Received data: " + convocationDTO);
        try {
            Optional<User> userOpt = userRepository.findByEmail(convocationDTO.getEmail());

            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            byte[] pdfContent = pdfService.generateConvocationPdf(user, convocationDTO);

            String subject = "Convocation - " + convocationDTO.getTitle();

            // Get email content from template service
            String messageText = EmailTemplateService.getConvocationEmailTemplate(
                    user.getPrenom(),
                    user.getNom(),
                    convocationDTO.getTitle()
            );

            String filename = "convocation_" + user.getNom().toLowerCase() + "_" +
                    convocationDTO.getTitle().replaceAll("\\s+", "_").toLowerCase() + ".pdf";

            emailService.sendConvocationEmail(user, subject, messageText, pdfContent, filename);

            return ResponseEntity.ok(Map.of("message", "Convocation envoyée avec succès à " + user.getEmail()));
        } catch (DocumentException | MessagingException e) {
            return ResponseEntity.internalServerError().body("Error sending convocation: " + e.getMessage());
        }
    }

    // Example endpoint to test PDF generation without sending email




}
