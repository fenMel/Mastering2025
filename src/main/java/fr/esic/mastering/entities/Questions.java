package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@Data
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texte; // Texte de la question

    @ElementCollection
    private List<String> options; // Liste des choix possibles

    private boolean obligatoire; // Indique si la question est obligatoire

    // Constructeur par défaut
    public Questions() {}

    // Constructeur personnalisé
    public Questions(String texte, List<String> options, boolean obligatoire) {
        this.texte = texte;
        this.options = options;
        this.obligatoire = obligatoire;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }
}
