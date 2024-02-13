package edu.java.bot.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLParser {

    private URLParser() {
    }

    public static URL parse(String url) {
        try {
            return new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new URLParseException();
        }
    }
}
