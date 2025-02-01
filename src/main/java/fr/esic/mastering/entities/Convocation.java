package fr.esic.mastering.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "convocations")
@Data
public class Convocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomEtudiant;
    private String titreProjet;
    private LocalDate date;
    private LocalTime heure;
    private String lieu;
    private String jury;
    private String tuteur;

    // // Constructeur par défaut
    // public Convocation() {}

    // // Constructeur avec tous les champs
    // public Convocation(Long id, String nomEtudiant, String titreProjet, LocalDate date, LocalTime heure, String lieu, String jury, String tuteur) {
    //     this.id = id;
    //     this.nomEtudiant = nomEtudiant;
    //     this.titreProjet = titreProjet;
    //     this.date = date;
    //     this.heure = heure;
    //     this.lieu = lieu;
    //     this.jury = jury;
    //     this.tuteur = tuteur;
    // }

    
    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getNomEtudiant() {
    //     return nomEtudiant;
    // }

    // public void setNomEtudiant(String nomEtudiant) {
    //     this.nomEtudiant = nomEtudiant;
    // }

    // public String getTitreProjet() {
    //     return titreProjet;
    // }

    // public void setTitreProjet(String titreProjet) {
    //     this.titreProjet = titreProjet;
    // }

    // public LocalDate getDate() {
    //     return date;
    // }

    // public void setDate(LocalDate date) {
    //     this.date = date;
    // }

    // public LocalTime getHeure() {
    //     return heure;
    // }

    // public void setHeure(LocalTime heure) {
    //     this.heure = heure;
    // }

    // public String getLieu() {
    //     return lieu;
    // }

    // public void setLieu(String lieu) {
    //     this.lieu = lieu;
    // }

    // public String getJury() {
    //     return jury;
    // }

    // public void setJury(String jury) {
    //     this.jury = jury;
    // }

    // public String getTuteur() {
    //     return tuteur;
    // }

    // public void setTuteur(String tuteur) {
    //     this.tuteur = tuteur;
    // }
}