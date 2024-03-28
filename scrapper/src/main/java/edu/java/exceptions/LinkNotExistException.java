package edu.java.exceptions;

public class LinkNotExistException extends Exception {
    private static final String DEFAULT_MESSAGE = "Link not exist";

    public LinkNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public LinkNotExistException(String message) {
        super(message);
    }
}
