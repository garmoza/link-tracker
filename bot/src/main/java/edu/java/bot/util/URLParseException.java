package edu.java.bot.util;

public class URLParseException extends Exception {

    public URLParseException(Exception cause) {
        super("Impossible to parse URL.", cause);
    }
}
