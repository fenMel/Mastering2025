package fr.esic.mastering.exceptions;

public class EmailAlreadyExistsException extends Throwable {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
