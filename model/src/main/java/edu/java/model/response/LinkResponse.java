package edu.java.model.response;

import java.net.URI;
import lombok.Builder;

@Builder
public record LinkResponse(
    long id,
    URI url
) {
}
