package edu.java.bot.entity;

import java.net.URL;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Link {

    private final String url;
    private final String host;

    public Link(String url, String host) {
        this.url = url;
        this.host = host;
    }

    public Link(URL url) {
        this.url = url.toExternalForm();
        this.host = url.getHost();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Link link = (Link) o;

        return Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
