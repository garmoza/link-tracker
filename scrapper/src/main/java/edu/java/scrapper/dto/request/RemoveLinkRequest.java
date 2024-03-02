package edu.java.scrapper.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
    @NotBlank
    String link
) {
}
