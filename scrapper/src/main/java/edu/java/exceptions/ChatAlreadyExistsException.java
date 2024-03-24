package edu.java.exceptions;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException() {
        super("This chat already exists!");
    }
}
