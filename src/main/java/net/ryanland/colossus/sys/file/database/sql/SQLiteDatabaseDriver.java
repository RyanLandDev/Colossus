package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.Colossus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQLite implementation of {@link SQLDatabaseDriver}
 */
public class SQLiteDatabaseDriver extends SQLDatabaseDriver {

    private Connection connection;

    /**
     * Create an instance of {@link SQLiteDatabaseDriver}
     * @param filePath The path to the database file (.db .sqlite) e.g. {@code src/database/db.sqlite}
     */
    public SQLiteDatabaseDriver(String filePath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (SQLException e) {
            Colossus.LOGGER.error("SQLite database connection failed", e);
            System.exit(0);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
