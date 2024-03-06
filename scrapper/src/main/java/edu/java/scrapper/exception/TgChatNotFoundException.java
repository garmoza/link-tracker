package edu.java.scrapper.exception;

public class TgChatNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Telegram Chat with id=%d not found";

    public TgChatNotFoundException(long tgChatId) {
        super(String.format(MESSAGE, tgChatId));
    }
}
