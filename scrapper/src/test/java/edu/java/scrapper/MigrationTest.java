package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationTest extends IntegrationTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @Test
    void tableExists_tg_chat() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet resultSet = metaData.getTables(null, null, "tg_chat", null);

        assertThat(resultSet.next()).as("check that the table exists").isTrue();
    }

    @Test
    void tableExists_trackable_link() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet resultSet = metaData.getTables(null, null, "trackable_link", null);

        assertThat(resultSet.next()).as("check that the table exists").isTrue();
    }

    @Test
    void tableExists_subscribe() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet resultSet = metaData.getTables(null, null, "subscribe", null);

        assertThat(resultSet.next()).as("check that the table exists").isTrue();
    }
}
