package edu.java.dto.database;

import edu.java.domain.jdbc.model.Link;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LinkDto {
    private Long id;
    private String url;
    private OffsetDateTime lastModified;
    private OffsetDateTime lastChecked;

    public static LinkDto fromJdbcLink(Link link) {
        return new LinkDto(
            link.getId(),
            link.getUrl(),
            link.getLastModifiedTime(),
            link.getLastCheckTime());
    }

    public static LinkDto fromJpaLink(edu.java.domain.jpa.model.Link link) {
        return new LinkDto(
            link.getId(),
            link.getUrl(),
            link.getLastModified(),
            link.getLastChecked());
    }
}
