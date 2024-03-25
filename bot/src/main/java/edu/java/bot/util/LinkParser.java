package edu.java.bot.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkParser {

    private LinkParser() {
    }

    public static String parse(String url) throws URLParseException {
        return parseURL(url).toString();
    }

    private static URL parseURL(String url) throws URLParseException {
        try {
            return new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new URLParseException(e);
        }
    }
}
