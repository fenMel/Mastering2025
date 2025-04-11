package fr.esic.mastering.api;

import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;

import fr.esic.mastering.dto.ConvocationDTO;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.UserRepository;
import fr.esic.mastering.services.EmailService;
import fr.esic.mastering.services.PdfGenerationService;

@RestController
@RequestMapping("/api/convocations")
@CrossOrigin(origins = "*")
public class ConvocationRest {

 
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PdfGenerationService pdfService;

    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendConvocation(@RequestBody ConvocationDTO convocationDTO) {
        System.out.println("Received data: " + convocationDTO);
        try {
            Optional<User> userOpt = userRepository.findById(convocationDTO.getUserId());
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            
            User user = userOpt.get();
            byte[] pdfContent = pdfService.generateConvocationPdf(user, convocationDTO);
            
            String subject = "Convocation - " + convocationDTO.getTitle();
            String messageText = "<p>Bonjour " + user.getNom() + " " + user.getPrenom() + ",</p>" +
                                  "<p>Veuillez trouver ci-joint votre convocation pour " + convocationDTO.getTitle() + ".</p>" +
                                  "<p>Cordialement,<br>Votre Organisation</p>";
            
            String filename = "convocation_" + user.getNom().toLowerCase() + "_" + 
                              convocationDTO.getTitle().replaceAll("\\s+", "_").toLowerCase() + ".pdf";
            
            emailService.sendConvocationEmail(user, subject, messageText, pdfContent, filename);
            
            return ResponseEntity.ok("Convocation sent successfully");
        } catch (DocumentException | MessagingException e) {
            return ResponseEntity.internalServerError().body("Error sending convocation: " + e.getMessage());
        }
    }
    
    // Example endpoint to test PDF generation without sending email
    @GetMapping("/test-pdf/{userId}")
    public ResponseEntity<?> testPdfGeneration(@PathVariable  Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            
            User user = userOpt.get();
            
            // Create test convocation data
            ConvocationDTO testConvocation = new ConvocationDTO();
            testConvocation.setUserId(userId);
            testConvocation.setTitle("Test Convocation");
            testConvocation.setLocation("Salle de Conférence A");
            testConvocation.setDateTime(LocalDateTime.now().plusDays(7));
            testConvocation.setAdditionalInfo("Ceci est un test de génération de PDF.");
            
            byte[] pdfContent = pdfService.generateConvocationPdf(user, testConvocation);
            
            // Return the PDF as a response
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=test_convocation.pdf")
                    .body(pdfContent);
            
        } catch (DocumentException e) {
            return ResponseEntity.internalServerError().body("Error generating PDF: " + e.getMessage());
        }
    }

}
