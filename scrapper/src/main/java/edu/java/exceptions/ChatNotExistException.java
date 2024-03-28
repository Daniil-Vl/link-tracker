package edu.java.exceptions;

public class ChatNotExistException extends Exception {
    private static final String DEFAULT_MESSAGE = "User not authenticated";

    public ChatNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public ChatNotExistException(String message) {
        super(message);
    }
}
