package edu.java.scrapper.exception;

public class LinkAlreadyExistsException extends RuntimeException {

    public LinkAlreadyExistsException() {
        super("Link already exists");
    }
}
