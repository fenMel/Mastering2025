package fr.esic.mastering.api;

import fr.esic.mastering.entities.Categorie;
import fr.esic.mastering.entities.Partenaire_pro;
import fr.esic.mastering.repository.CategorieRepository;
import fr.esic.mastering.repository.PartenaireProRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partenaires")
public class PartenaireProfessionnelController {

    @Autowired
    private PartenaireProRepository partenaireRepo;

    @Autowired
    private CategorieRepository categorieRepo;

    //  GET : Tous les partenaires
    @GetMapping
    public List<Partenaire_pro> getAll() {
        return partenaireRepo.findAll();
    }

    //  GET : Un partenaire par ID
    @GetMapping("/{id}")
    public ResponseEntity<Partenaire_pro> getById(@PathVariable Long id) {
        Optional<Partenaire_pro> partenaire = partenaireRepo.findById(id);
        return partenaire.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  POST : Créer un nouveau partenaire
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Partenaire_pro partenaire) {
        if (partenaire.getCategorie() == null || partenaire.getCategorie().getId() == null) {
            return ResponseEntity.badRequest().body("Catégorie requise");
        }

        Optional<Categorie> categorie = categorieRepo.findById(partenaire.getCategorie().getId());
        if (categorie.isEmpty()) {
            return ResponseEntity.badRequest().body("Catégorie inexistante");
        }

        partenaire.setCategorie(categorie.get());
        return ResponseEntity.ok(partenaireRepo.save(partenaire));
    }

    //PUT : Modifier un partenaire existant
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Partenaire_pro partenaire) {
        Optional<Partenaire_pro> existing = partenaireRepo.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Categorie> categorie = categorieRepo.findById(partenaire.getCategorie().getId());
        if (categorie.isEmpty()) {
            return ResponseEntity.badRequest().body("Catégorie inexistante");
        }

        Partenaire_pro toUpdate = existing.get();
        toUpdate.setRaisonSociale(partenaire.getRaisonSociale());
        toUpdate.setAdresse(partenaire.getAdresse());
        toUpdate.setTelephone(partenaire.getTelephone());
        toUpdate.setMail(partenaire.getMail());
        toUpdate.setCategorie(categorie.get());

        return ResponseEntity.ok(partenaireRepo.save(toUpdate));
    }

    //  DELETE : Supprimer un partenaire
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!partenaireRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        partenaireRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
