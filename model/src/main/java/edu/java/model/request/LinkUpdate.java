package edu.java.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdate(
    @NotBlank
    String url,
    @NotNull
    String description,
    @NotEmpty
    List<Long> tgChatIds
) {
}
