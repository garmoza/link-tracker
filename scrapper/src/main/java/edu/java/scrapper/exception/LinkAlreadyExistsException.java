package edu.java.scrapper.exception;

import edu.java.model.response.LinkResponse;
import lombok.Getter;

@Getter
public class LinkAlreadyExistsException extends RuntimeException {

    private final LinkResponse link;

    public LinkAlreadyExistsException(LinkResponse link) {
        super("Link already exists");
        this.link = link;
    }
}
