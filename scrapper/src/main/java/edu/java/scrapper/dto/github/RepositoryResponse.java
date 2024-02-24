package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record RepositoryResponse(
    String name,
    @JsonAlias({"updated_at"})
    OffsetDateTime updatedAt,
    @JsonAlias({"pushed_at"})
    OffsetDateTime pushedAt
) {
}
