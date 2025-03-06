package fr.esic.mastering.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.entities.RoleType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.repository.UserRepository;

@Service
public class CertificatService {

    /**
     * Modèle de certificat de réussite
     */
    public static class CertificatMaster {
        private String nomEtudiant;
        private String prenomEtudiant;
        private String specialite;
        private String mention;
        private String dateObtention;
        private String numCertificat;
        private String titreMaster;
        private String directeurProgramme;
        
        public CertificatMaster() {
            this.dateObtention = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
            this.numCertificat = "M-" + System.currentTimeMillis();
        }

        // Getters et setters
        public String getNomEtudiant() {
            return nomEtudiant;
        }

        public void setNomEtudiant(String nomEtudiant) {
            this.nomEtudiant = nomEtudiant;
        }

        public String getPrenomEtudiant() {
            return prenomEtudiant;
        }

        public void setPrenomEtudiant(String prenomEtudiant) {
            this.prenomEtudiant = prenomEtudiant;
        }

        public String getSpecialite() {
            return specialite;
        }

        public void setSpecialite(String specialite) {
            this.specialite = specialite;
        }

        public String getMention() {
            return mention;
        }

        public void setMention(String mention) {
            this.mention = mention;
        }

        public String getDateObtention() {
            return dateObtention;
        }

        public void setDateObtention(String dateObtention) {
            this.dateObtention = dateObtention;
        }

        public String getNumCertificat() {
            return numCertificat;
        }

        public void setNumCertificat(String numCertificat) {
            this.numCertificat = numCertificat;
        }

        public String getTitreMaster() {
            return titreMaster;
        }

        public void setTitreMaster(String titreMaster) {
            this.titreMaster = titreMaster;
        }

        public String getDirecteurProgramme() {
            return directeurProgramme;
        }

        public void setDirecteurProgramme(String directeurProgramme) {
            this.directeurProgramme = directeurProgramme;
        }
    }

    /**
     * Génère un certificat de réussite pour une soutenance de Master
     * 
     * @param certificat Les informations du certificat
     * @return Le PDF du certificat
     * @throws DocumentException
     */
    public ByteArrayInputStream genererCertificatMaster(CertificatMaster certificat) throws DocumentException {
        // Créer un document au format A4 paysage
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        try {
            // Ajouter une bordure décorative
            Rectangle rect = new Rectangle(document.left() + 15, document.bottom() + 15, 
                              document.right() - 15, document.top() - 15);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(2);
            rect.setBorderColor(new java.awt.Color(28, 40, 94)); // Bleu foncé
            writer.setBoxSize("art", rect);
            
            // Logo de l'université (à remplacer par le chemin réel de votre logo)
            // Image logo = Image.getInstance("classpath:static/images/logo_universite.png");
            // logo.scaleToFit(150, 150);
            // logo.setAbsolutePosition(document.left() + 30, document.top() - 100);
            // document.add(logo);

            // Titre principal
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26);
            titleFont.setColor(new java.awt.Color(28, 40, 94)); // Bleu foncé
            Paragraph title = new Paragraph("CERTIFICAT DE RÉUSSITE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(60);
            title.setSpacingAfter(20);
            document.add(title);

            // Sous-titre
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Paragraph subtitle = new Paragraph("Master " + certificat.getTitreMaster(), subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(40);
            document.add(subtitle);

            // Corps du certificat
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Paragraph body = new Paragraph();
            body.setFont(bodyFont);
            body.setAlignment(Element.ALIGN_CENTER);
            body.setLeading(22f);
            body.add("Ce certificat atteste que\n\n");
            document.add(body);

            // Nom de l'étudiant
            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph name = new Paragraph(certificat.getPrenomEtudiant() + " " + certificat.getNomEtudiant().toUpperCase(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(30);
            document.add(name);

            // Suite du texte
            Paragraph suite = new Paragraph();
            suite.setFont(bodyFont);
            suite.setAlignment(Element.ALIGN_CENTER);
            suite.setLeading(22f);
            String texte = "a soutenu avec succès sa thèse de Master\n"
                    + "en " + certificat.getSpecialite() + "\n"
                    + "avec la mention " + certificat.getMention() + "\n\n"
                    + "délivré le " + certificat.getDateObtention() + "\n"
                    + "Numéro de certificat : " + certificat.getNumCertificat();
            suite.add(texte);
            document.add(suite);

            // Espace pour signature
            Paragraph signature = new Paragraph("\n\n\n");
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            // Signature du directeur
            Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph signatureName = new Paragraph("Le Directeur du Programme,\n" + certificat.getDirecteurProgramme(), signatureFont);
            signatureName.setAlignment(Element.ALIGN_RIGHT);
            signatureName.setIndentationRight(40);
            document.add(signatureName);

        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
     @Autowired
    private UserRepository userRepository;
    /**
     * Génère un certificat pour un utilisateur spécifique à partir de son ID
     */
    public ByteArrayInputStream genererCertificatPourUser(Long userId) throws DocumentException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier que l'utilisateur est bien un candidat
        if (user.getRole().getRoleUtilisateur() != RoleType.CANDIDATE) {
            throw new RuntimeException("Le certificat ne peut être généré que pour un candidat");
        }
        
        CertificatMaster certificat = new CertificatMaster();
        certificat.setNomEtudiant(user.getNom());
        certificat.setPrenomEtudiant(user.getPrenom());
        
        // Récupérer la spécialité depuis la formation
        String specialite = user.getFormation() != null ? 
                            user.getFormation().getNom() : 
                            "Non spécifiée";
        certificat.setSpecialite(specialite);
        
        // Définir une mention par défaut (à personnaliser selon votre logique)
        certificat.setMention("Bien");
        
        certificat.setTitreMaster("Sciences et Technologies");
        certificat.setDirecteurProgramme("Prof. Jean DUPONT");
        
        return genererCertificatMaster(certificat);
    }
    
    /**
     * Récupère tous les candidats
     */
    public List<User> getAllCandidats() {
        return userRepository.findByRoleRoleUtilisateur(RoleType.CANDIDATE);
    }
    
}