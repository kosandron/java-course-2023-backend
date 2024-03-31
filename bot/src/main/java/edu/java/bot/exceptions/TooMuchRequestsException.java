package edu.java.bot.exceptions;

public class TooMuchRequestsException extends RuntimeException {
    public TooMuchRequestsException() {
        super("Too much request done!");
    }
}
