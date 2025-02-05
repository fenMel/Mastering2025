package fr.esic.mastering.exceptions;

@SuppressWarnings("serial")
public class Erreurs extends RuntimeException {
    public Erreurs(String message) {
        super(message);
    }
}
