package edu.java.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super("Resource was not found!");
    }

    public static ResourceNotFoundException chatNotFoundException(long chatId) {
        return new ResourceNotFoundException("Chat with id %d was not found!".formatted(chatId));
    }

    public static ResourceNotFoundException linkNotFoundException(long linkId) {
        return new ResourceNotFoundException("Link with id %d was not found!".formatted(linkId));
    }
}
