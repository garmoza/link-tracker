package edu.java.scrapper.exception;

public class TgChatAlreadyExistsException extends RuntimeException {

    public TgChatAlreadyExistsException() {
        super("Telegram chat already exists");
    }
}
