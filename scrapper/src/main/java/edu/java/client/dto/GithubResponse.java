package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubResponse(
    @JsonProperty("title") String title,
    @JsonProperty("updated_at") OffsetDateTime lastModified
) {
}
