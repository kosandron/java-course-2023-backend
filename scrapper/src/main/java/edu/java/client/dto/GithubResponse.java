package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubResponse(
    String title,
    @JsonProperty("updated_at") OffsetDateTime lastModified
) {
}
