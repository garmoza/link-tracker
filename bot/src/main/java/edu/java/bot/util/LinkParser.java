package edu.java.bot.util;

import edu.java.bot.entity.Link;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkParser {

    private LinkParser() {
    }

    public static Link parse(String url) throws URLParseException {
        return new Link(parseURL(url));
    }

    private static URL parseURL(String url) throws URLParseException {
        try {
            return new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new URLParseException(e);
        }
    }
}
