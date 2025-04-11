package fr.esic.mastering.services;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import fr.esic.mastering.dto.ConvocationDTO;
import fr.esic.mastering.entities.User;

@Service
public class PdfGenerationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generateConvocationPdf(User user, ConvocationDTO convocation) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        // Add header with logo
        addHeader(document);
        
        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("CONVOCATION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Add recipient details
        document.add(new Paragraph("À l'attention de : " + user.getNom() + " " + user.getPrenom()));
        document.add(new Paragraph("Email : " + user.getEmail()));
        document.add(new Paragraph("Date : " + convocation.getDateTime().format(DATE_FORMATTER)));
        document.add(new Paragraph("Lieu : " + convocation.getLocation()));
        document.add(new Paragraph("\n"));
        
        // Add specific content based on user type
        switch (user.getRole().getRoleUtilisateur()) {
            case JURY:
                addJuryContent(document, convocation);
                break;
            case CANDIDAT:
                addCandidateContent(document, convocation);
                break;
            case CORDINATEUR:
                addCoordinatorContent(document, convocation);
                break;
                case APPRENANT:
                addCoordinatorContent(document, convocation);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        }
        
        // Add additional information
        document.add(new Paragraph("\nInformations complémentaires:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        document.add(new Paragraph(convocation.getAdditionalInfo()));
        
        // Add footer
        addFooter(document);
        
        document.close();
        return outputStream.toByteArray();
    }
    
    private void addHeader(Document document) throws DocumentException {
        // Add your organization logo and header information
        Paragraph header = new Paragraph("Your Organization Name");
        header.setAlignment(Element.ALIGN_RIGHT);
        document.add(header);
        document.add(new Paragraph("\n"));
    }
    
    private void addJuryContent(Document document, ConvocationDTO convocation) throws DocumentException {
        Paragraph content = new Paragraph(
            "Vous êtes convoqué(e) en tant que membre du jury pour " + convocation.getTitle() + ".\n\n" +
            "Votre rôle sera d'évaluer les candidats selon les critères établis. " +
            "Veuillez vous présenter 30 minutes avant le début de la session pour une réunion préparatoire."
        );
        document.add(content);
    }
    
    private void addCandidateContent(Document document, ConvocationDTO convocation) throws DocumentException {
        Paragraph content = new Paragraph(
            "Vous êtes convoqué(e) en tant que candidat(e) pour " + convocation.getTitle() + ".\n\n" +
            "Veuillez vous présenter 15 minutes avant l'heure indiquée et vous munir de votre pièce d'identité " +
            "ainsi que des documents demandés lors de votre inscription."
        );
        document.add(content);
    }
    
    
    private void addCoordinatorContent(Document document, ConvocationDTO convocation) throws DocumentException {
        Paragraph content = new Paragraph(
            "Vous êtes convoqué(e) en tant que coordinateur(trice) pour " + convocation.getTitle() + ".\n\n" +
            "Votre rôle sera de superviser le bon déroulement de la session. " +
            "Veuillez vous présenter 1 heure avant le début pour préparer la salle et accueillir les membres du jury."
        );
        document.add(content);
    }
    
    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
            "\n\nVeuillez confirmer votre présence en répondant à cet email." +
            "\nEn cas d'empêchement, merci de nous contacter au plus tôt au 01 XX XX XX XX."
        );
        document.add(footer);
    }
}