package edu.java.domain.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastModifiedTime;
    private OffsetDateTime lastCheckTime;

    public static Link parseResultSet(ResultSet rs) throws SQLException {
        return Link.builder()
            .id(rs.getLong("id"))
            .url(rs.getString("url"))
            .lastModifiedTime(rs.getObject("last_modified_time", OffsetDateTime.class))
            .lastCheckTime(rs.getObject("last_check_time", OffsetDateTime.class))
            .build();
    }
}
