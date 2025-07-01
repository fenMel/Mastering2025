package fr.esic.mastering.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partenaire_pro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raisonSociale;

    private String adresse;

    private String telephone;

    private String mail;

    @ManyToOne
    @JoinColumn(name = "categorie_id") // clé étrangère
    private Categorie categorie;
}
