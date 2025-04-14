package fr.esic.mastering.services;


public class UserProfileForm {
    // Common fields for all users
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String lieuxDeNaissance;
    private String dateNaissance;

    // Role-specific fields
    private String specialization; // For JURY
    private String availability;   // For JURY

    private String cv;             // For CANDIDAT
    private String motivationLetter; // For CANDIDAT

    private String department;     // For CORDINATEUR

    // Getters and setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLieuxDeNaissance() {
        return lieuxDeNaissance;
    }

    public void setLieuxDeNaissance(String lieuxDeNaissance) {
        this.lieuxDeNaissance = lieuxDeNaissance;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getMotivationLetter() {
        return motivationLetter;
    }

    public void setMotivationLetter(String motivationLetter) {
        this.motivationLetter = motivationLetter;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

