package fr.esic.mastering.api;


import fr.esic.mastering.entities.Categorie;
import fr.esic.mastering.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping
    public List<Categorie> getAll() {
        return categorieRepository.findAll();
    }

    @PostMapping
    public Categorie create(@RequestBody Categorie categorie) {
        return categorieRepository.save(categorie);
    }
}
