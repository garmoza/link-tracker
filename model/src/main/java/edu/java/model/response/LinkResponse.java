package edu.java.model.response;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url
) {
}
