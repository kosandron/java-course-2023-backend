package edu.java.domain.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Chat {
    private Long chatId;

    public static Chat parseResultSet(ResultSet rs) throws SQLException {
        return Chat.builder().chatId(rs.getLong("id")).build();
    }
}
