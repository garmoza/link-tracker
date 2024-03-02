package edu.java.bot.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
