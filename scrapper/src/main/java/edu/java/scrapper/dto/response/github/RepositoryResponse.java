package edu.java.scrapper.dto.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record RepositoryResponse(
    @JsonProperty("name") String name,
    @JsonProperty("updated_at") OffsetDateTime updatedAt,
    @JsonProperty("pushed_at") OffsetDateTime pushedAt
) {
}
