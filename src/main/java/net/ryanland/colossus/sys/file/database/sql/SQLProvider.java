package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQLProvider extends Provider<HashMap<String, Object>, ResultSet> {

    @Override
    public final Supply deserialize(ResultSet data) {
        try {
            return deserializeSQL(data);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public abstract Supply deserializeSQL(ResultSet data) throws SQLException;
}
