package fr.esic.mastering.services;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import fr.esic.mastering.dto.ConvocationDTO;
import fr.esic.mastering.entities.User;

import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.InputStream;



@Service
public class PdfGenerationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final BaseColor PRIMARY_COLOR = new BaseColor(0, 102, 204); // Blue
    private static final BaseColor SECONDARY_COLOR = new BaseColor(80, 80, 80); // Dark gray

    public byte[] generateConvocationPdf(User user, ConvocationDTO convocation) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add header with logo
        addHeader(document);

        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, PRIMARY_COLOR);
        Paragraph title = new Paragraph("CONVOCATION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add recipient information in a box
        addRecipientInfo(document, user, convocation);

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
        }

        // Add additional information section if available
        if (convocation.getAdditionalInfo() != null && !convocation.getAdditionalInfo().trim().isEmpty()) {
            addAdditionalInfo(document, convocation);
        }

        // Add footer
        addFooter(document);

        document.close();
        return outputStream.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        try {
            // Create a table for layout (2 columns: logo and title)
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[] {1f, 2f});
            headerTable.setSpacingAfter(20);

            // First cell for the logo
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);

            // Add logo
            try {
                // Get logo from classpath resources
                InputStream logoStream = getClass().getResourceAsStream("/static/images/logo_mastering.png");
                if (logoStream != null) {
                    byte[] logoBytes = logoStream.readAllBytes();
                    Image logo = Image.getInstance(logoBytes);
                    logo.scaleToFit(100, 100);
                    logoCell.addElement(logo);
                    logoStream.close();
                } else {
                    // Fallback if logo not found
                    Font logoFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR);
                    Paragraph logoText = new Paragraph("MASTERING", logoFont);
                    logoText.setAlignment(Element.ALIGN_CENTER);
                    logoCell.addElement(logoText);
                }
            } catch (IOException e) {
                // Fallback to text if image can't be loaded
                Font logoFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR);
                Paragraph logoText = new Paragraph("MASTERING", logoFont);
                logoText.setAlignment(Element.ALIGN_CENTER);
                logoCell.addElement(logoText);
            }

            headerTable.addCell(logoCell);

            // Second cell for company info
            PdfPCell infoCell = getPdfPCell();

            headerTable.addCell(infoCell);

            // Add the table to the document
            document.add(headerTable);

            // Add a horizontal line
            LineSeparator line = new LineSeparator();
            line.setLineColor(PRIMARY_COLOR);
            line.setLineWidth(1);
            document.add(line);

            // Add some space after the line
            document.add(new Paragraph("\n"));

        } catch (Exception e) {
            // Fallback to simple header if there's an error
            Paragraph header = new Paragraph("Mastering", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR));
            header.setAlignment(Element.ALIGN_RIGHT);
            document.add(header);
            document.add(new Paragraph("\n"));
        }
    }

    private static PdfPCell getPdfPCell() {
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setPaddingLeft(10);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Add company name with colored, larger font
        Font companyNameFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, PRIMARY_COLOR);
        Paragraph companyName = new Paragraph("MASTERING", companyNameFont);
        companyName.setAlignment(Element.ALIGN_LEFT);
        infoCell.addElement(companyName);

        // Add additional company information
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, SECONDARY_COLOR);
        Paragraph companyInfo = new Paragraph(
                """
                        123 Business VAUGIRARD
                        75001 Paris, France
                        Tél: +33 1 23 45 67 89
                        Email: contact@mastering.com""", infoFont);
        companyInfo.setAlignment(Element.ALIGN_LEFT);
        infoCell.addElement(companyInfo);
        return infoCell;
    }

    private void addRecipientInfo(Document document, User user, ConvocationDTO convocation) throws DocumentException {
        // Create a bordered box for recipient information
        PdfPTable infoTable = new PdfPTable(1);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorderColor(PRIMARY_COLOR);
        infoCell.setPadding(10);

        // Add recipient details
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);

        Paragraph recipientInfo = new Paragraph();
        recipientInfo.add(new Chunk("À l'attention de : ", boldFont));
        recipientInfo.add(new Chunk(user.getNom() + " " + user.getPrenom(), normalFont));
        recipientInfo.add(Chunk.NEWLINE);

        recipientInfo.add(new Chunk("Email : ", boldFont));
        recipientInfo.add(new Chunk(user.getEmail(), normalFont));
        recipientInfo.add(Chunk.NEWLINE);

        // Handle dateTime with null check
        recipientInfo.add(new Chunk("Date : ", boldFont));
        if (convocation.getDateTime() != null) {
            recipientInfo.add(new Chunk(convocation.getDateTime().format(DATE_FORMATTER), normalFont));
        } else {
            recipientInfo.add(new Chunk("Non spécifiée", normalFont));
        }
        recipientInfo.add(Chunk.NEWLINE);

        // Handle location with null check
        recipientInfo.add(new Chunk("Lieu : ", boldFont));
        recipientInfo.add(new Chunk(convocation.getLocation() != null ? convocation.getLocation() : "Non spécifié", normalFont));

        infoCell.addElement(recipientInfo);
        infoTable.addCell(infoCell);

        document.add(infoTable);
    }

    private void addJuryContent(Document document, ConvocationDTO convocation) throws DocumentException {
        // Add role heading
        Font roleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, PRIMARY_COLOR);
        Paragraph roleHeading = new Paragraph("Votre rôle : Membre du Jury", roleFont);
        roleHeading.setSpacingBefore(10);
        roleHeading.setSpacingAfter(10);
        document.add(roleHeading);

        // Add content
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph content = new Paragraph(
                "Vous êtes convoqué(e) en tant que membre du jury pour " + convocation.getTitle() + ".\n\n" +
                        "Votre rôle sera d'évaluer les candidats selon les critères établis. " +
                        "Veuillez vous présenter 30 minutes avant le début de la session pour une réunion préparatoire.", contentFont);
        document.add(content);

        // Add responsibilities in bullet points
        Paragraph responsibilities = new Paragraph();
        responsibilities.setSpacingBefore(15);
        responsibilities.add(new Chunk("Vos responsabilités :", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        responsibilities.add(Chunk.NEWLINE);

        List list = new List(List.UNORDERED);
        list.setIndentationLeft(20);
        list.add(new ListItem("Évaluer les candidats selon la grille d'évaluation fournie", contentFont));
        list.add(new ListItem("Participer aux délibérations du jury", contentFont));
        list.add(new ListItem("Rendre un avis motivé pour chaque candidat", contentFont));
        list.add(new ListItem("Respecter les principes d'équité et de confidentialité", contentFont));

        responsibilities.add(list);
        document.add(responsibilities);
    }

    private void addCandidateContent(Document document, ConvocationDTO convocation) throws DocumentException {
        // Add role heading
        Font roleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, PRIMARY_COLOR);
        Paragraph roleHeading = new Paragraph(" Infos session", roleFont);
        roleHeading.setSpacingBefore(10);
        roleHeading.setSpacingAfter(10);
        document.add(roleHeading);

        // Add content
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph content = new Paragraph(
                "Vous êtes convoqué(e) en tant que candidat(e) pour " + convocation.getTitle() + ".\n\n" +
                        "Veuillez vous présenter 15 minutes avant l'heure indiquée et vous munir de votre pièce d'identité " +
                        "ainsi que des documents demandés lors de votre inscription.", contentFont);
        document.add(content);

        // Add items to bring in bullet points
        Paragraph itemsToBring = new Paragraph();
        itemsToBring.setSpacingBefore(15);
        itemsToBring.add(new Chunk("Documents à apporter :", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        itemsToBring.add(Chunk.NEWLINE);

        List list = new List(List.UNORDERED);
        list.setIndentationLeft(20);
        list.add(new ListItem("Pièce d'identité (obligatoire)", contentFont));
        list.add(new ListItem("Convocation imprimée", contentFont));
        list.add(new ListItem("CV à jour", contentFont));
        list.add(new ListItem("Portfolio ou exemples de travaux (si applicable)", contentFont));
        list.add(new ListItem("Tout autre document spécifié dans votre dossier d'inscription", contentFont));

        itemsToBring.add(list);
        document.add(itemsToBring);
    }

    private void addCoordinatorContent(Document document, ConvocationDTO convocation) throws DocumentException {
        // Add role heading
        Font roleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, PRIMARY_COLOR);
        Paragraph roleHeading = new Paragraph("Votre rôle : Coordinateur", roleFont);
        roleHeading.setSpacingBefore(10);
        roleHeading.setSpacingAfter(10);
        document.add(roleHeading);

        // Add content
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph content = new Paragraph(
                "Vous êtes convoqué(e) en tant que coordinateur(trice) pour " + convocation.getTitle() + ".\n\n" +
                        "Votre rôle sera de superviser le bon déroulement de la session. " +
                        "Veuillez vous présenter 1 heure avant le début pour préparer la salle et accueillir les membres du jury.", contentFont);
        document.add(content);

        // Add responsibilities in bullet points
        Paragraph responsibilities = new Paragraph();
        responsibilities.setSpacingBefore(15);
        responsibilities.add(new Chunk("Vos responsabilités :", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        responsibilities.add(Chunk.NEWLINE);

        List list = new List(List.UNORDERED);
        list.setIndentationLeft(20);
        list.add(new ListItem("Préparer la salle et le matériel nécessaire", contentFont));
        list.add(new ListItem("Accueillir les membres du jury et les candidats", contentFont));
        list.add(new ListItem("Veiller au respect du planning", contentFont));
        list.add(new ListItem("Assurer la liaison entre les différentes parties prenantes", contentFont));
        list.add(new ListItem("Collecter et sécuriser les documents d'évaluation", contentFont));

        responsibilities.add(list);
        document.add(responsibilities);
    }

    private void addAdditionalInfo(Document document, ConvocationDTO convocation) throws DocumentException {
        // Add a horizontal line
        LineSeparator line = new LineSeparator();
        line.setLineColor(new BaseColor(200, 200, 200)); // Light gray
        line.setLineWidth(0.5f);
        line.setPercentage(100);
        line.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph(" "));
        document.add(line);

        // Add additional information heading
        Font headingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, PRIMARY_COLOR);
        Paragraph heading = new Paragraph("Informations complémentaires", headingFont);
        heading.setSpacingBefore(10);
        heading.setSpacingAfter(5);
        document.add(heading);

        // Add the additional information
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph additionalInfo = new Paragraph(convocation.getAdditionalInfo(), contentFont);
        document.add(additionalInfo);
    }

    private void addFooter(Document document) throws DocumentException {
        // Add some space
        document.add(new Paragraph("\n"));

        // Add a horizontal line
        LineSeparator line = new LineSeparator();
        line.setLineColor(PRIMARY_COLOR);
        line.setLineWidth(0.5f);
        document.add(line);

        // Add footer content
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, SECONDARY_COLOR);

        Paragraph confirmation = new Paragraph(
                """

                        Veuillez confirmer votre présence en répondant à cet email.\

                        En cas d'empêchement, merci de nous contacter au plus tôt au +33 1 23 45 67 89.""",
                footerFont
        );
        document.add(confirmation);

        // Add copyright and additional information
        Paragraph copyright = new Paragraph(
                "\n© " + java.time.Year.now().getValue() + " Mastering. Tous droits réservés.",
                footerFont
        );
        copyright.setAlignment(Element.ALIGN_CENTER);
        document.add(copyright);
    }
}