package edu.java.exceptions;

public class ChatNotExistException extends Exception {
    private static final String defaultMessage = "User not authenticated";

    public ChatNotExistException() {
        super(defaultMessage);
    }

    public ChatNotExistException(String message) {
        super(message);
    }
}
