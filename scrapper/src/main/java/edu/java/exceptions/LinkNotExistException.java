package edu.java.exceptions;

public class LinkNotExistException extends Exception {
    private static final String defaultMessage = "Link not exist";

    public LinkNotExistException() {
        super(defaultMessage);
    }

    public LinkNotExistException(String message) {
        super(message);
    }
}
