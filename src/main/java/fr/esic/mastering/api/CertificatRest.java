package fr.esic.mastering.api;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;

import fr.esic.mastering.entities.User;
import fr.esic.mastering.services.CertificatService;
import fr.esic.mastering.services.CertificatService.CertificatMaster;

@RestController
@RequestMapping("/api/certificats")
public class CertificatRest {

    @Autowired
    private CertificatService certificatService;

    /**
     * Génère un certificat de réussite avec les informations fournies
     * 
     * @param certificat Les informations complètes du certificat
     * @return Le PDF du certificat
     * @throws DocumentException
     */
    @PostMapping("/master")
    public ResponseEntity<InputStreamResource> genererCertificatMaster(
            @RequestBody CertificatMaster certificat) throws DocumentException {
        
        ByteArrayInputStream bis = certificatService.genererCertificatMaster(certificat);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=certificat-master.pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    


    @GetMapping("/candidats")
    public ResponseEntity<List<User>> getAllCandidats() {
        List<User> candidats = certificatService.getAllCandidats();
        return ResponseEntity.ok(candidats);
    }

// Génère un certificat pour un utilisateur spécifique
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<InputStreamResource> genererCertificatUser(
            @PathVariable Long userId) throws DocumentException {
        
        ByteArrayInputStream bis = certificatService.genererCertificatPourUser(userId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=certificat-user-" + userId + ".pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
    /**
     * Génère un certificat pour un utilisateur et le propose en téléchargement
     */
    @GetMapping("/user/{userId}/download")
    public ResponseEntity<InputStreamResource> telechargerCertificatUser(
            @PathVariable Long userId) throws DocumentException {
        
        ByteArrayInputStream bis = certificatService.genererCertificatPourUser(userId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=certificat-user-" + userId + ".pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
    /**
     * Version simplifiée pour générer un certificat avec juste le nom et prénom
     * 
     * @param nom Le nom de l'étudiant
     * @param prenom Le prénom de l'étudiant
     * @param specialite La spécialité du Master
     * @param mention La mention obtenue
     * @return Le PDF du certificat
     * @throws DocumentException
     */
    @GetMapping("/master/simple")
    public ResponseEntity<InputStreamResource> genererCertificatSimple(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam(defaultValue = "Informatique") String specialite,
            @RequestParam(defaultValue = "Bien") String mention) throws DocumentException {
        
        CertificatMaster certificat = new CertificatMaster();
        certificat.setNomEtudiant(nom);
        certificat.setPrenomEtudiant(prenom);
        certificat.setSpecialite(specialite);
        certificat.setMention(mention);
        certificat.setTitreMaster("Sciences et Technologies");
        certificat.setDirecteurProgramme("Prof. Jean DUPONT");
        
        ByteArrayInputStream bis = certificatService.genererCertificatMaster(certificat);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=certificat-" + nom + "-" + prenom + ".pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/master/view")
public ResponseEntity<InputStreamResource> visualiserCertificat(
        @RequestParam String nom,
        @RequestParam String prenom,
        @RequestParam(defaultValue = "Informatique") String specialite,
        @RequestParam(defaultValue = "Bien") String mention) throws DocumentException {
    
    CertificatMaster certificat = new CertificatMaster();
    certificat.setNomEtudiant(nom);
    certificat.setPrenomEtudiant(prenom);
    certificat.setSpecialite(specialite);
    certificat.setMention(mention);
    certificat.setTitreMaster("Sciences et Technologies");
    certificat.setDirecteurProgramme("Prof. Jean DUPONT");
    
    ByteArrayInputStream bis = certificatService.genererCertificatMaster(certificat);
    
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "inline; filename=certificat-" + nom + "-" + prenom + ".pdf");
    
    return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(bis));
}

}