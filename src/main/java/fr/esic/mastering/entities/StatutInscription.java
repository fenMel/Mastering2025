package fr.esic.mastering.entities;

public enum StatutInscription {
    APPRENANT,
    EN_ATTENTE,    // Statut initial
    VALIDEE,       // Validée par l'admin
    REFUSEE,      // Refusée par l'admin
    EN_VERIFICATION
}