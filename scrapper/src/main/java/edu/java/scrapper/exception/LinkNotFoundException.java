package edu.java.scrapper.exception;

public class LinkNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Link '%s' not found";

    public LinkNotFoundException(String link) {
        super(String.format(MESSAGE, link));
    }
}
