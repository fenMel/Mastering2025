package fr.esic.mastering.exceptions;



public class InscriptionNotFoundException extends RuntimeException {
    public InscriptionNotFoundException(Long inscriptionId) {
        super("Inscription non trouvée avec l'id: " + inscriptionId);
    }
}
