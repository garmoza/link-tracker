package edu.java.scrapper.dto.request;

import java.util.List;

public record LinkUpdate(
    long id,
    String url,
    String description,
    List<Long> tgChatIds
) {
}
