package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record LinkUpdate(
    long id,
    @NotBlank
    String url,
    String description,
    List<Long> tgChatIds
) {
}
