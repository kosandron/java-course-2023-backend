package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseMigrationTest extends IntegrationTest {

    @Test
    public void createContainerTest() throws SQLException {
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );

        assertThat(tableExists(connection, "links")).isTrue();
        assertThat(tableExists(connection, "chats")).isTrue();
        assertThat(tableExists(connection, "chats_links")).isTrue();
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet rs = connection
            .getMetaData()
            .getTables(null, null, tableName, new String[] {"TABLE"});
        return rs.next();
    }
}
