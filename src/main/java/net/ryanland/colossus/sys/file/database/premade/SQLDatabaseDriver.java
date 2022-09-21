package net.ryanland.colossus.sys.file.database.premade;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import net.ryanland.colossus.sys.file.database.Stock;
import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * SQL implementation of {@link DatabaseDriver}.<br>
 *
 * <p><b>Important note:</b> SQL tables won't be created for you, the driver will only update them. Create them beforehand.
 * <br>The following tables + columns should exist <i>at least</i>:
 * <ul>
 * <li>{@code global}
 *     <ul>
 *         <li>{@code _bot_id - PK varchar(25)}</li>
 *     </ul></li>
 * <li>{@code guilds}
 *     <ul>
 *         <li>{@code _guild_id - PK varchar(25)}</li>
 *         <li>{@code prefix - varchar(?)} <i>if guild prefixes are used - max length is up to you</i></li>
 *     </ul></li>
 * <li>{@code members}
 *     <ul>
 *         <li>{@code _user_id - PK NN varchar(25)}</li>
 *         <li>{@code _guild_id - PK NN varchar(25)}</li>
 *     </ul></li>
 * <li>{@code users}
 *     <ul>
 *         <li>{@code _user_id - PK varchar(25)}</li>
 *     </ul></li>
 * <li>{@code cooldowns}
 *     <ul>
 *         <li>{@code user_id - PK NN varchar(25)}</li>
 *         <li>{@code command_name - PK NN varchar(32)}</li>
 *         <li>{@code expires - NN datetime}</li>
 *         <li>{@code command_type - PK NN tinyint}</li>
 *     </ul></li>
 * <li>{@code disabled_commands}
 *     <ul>
 *         <li>{@code command_name - PK NN varchar(32)}</li>
 *         <li>{@code command_type - PK NN tinyint}</li>
 *     </ul></li>
 * </ul>
 */
public abstract class SQLDatabaseDriver extends DatabaseDriver {

    public abstract Connection getConnection();

    public final ResultSet query(String query) {
        try {
            return query(getConnection().prepareStatement(query));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public final ResultSet query(PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public final ResultSet query(String query, Object... params) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            int i = 0;
            for (Object param : params) {
                i++;
                ps.setObject(i, param);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Performs an SQL query and returns the value of the first column
     */
    @SuppressWarnings("unchecked")
    public final <R> R singleQuery(String query) {
        try {
            return (R) query(query).getObject(1);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /*
    private Set<String> getPrimaryKeys(String tableName) throws SQLException {
        ResultSet pkColumns = getConnection().getMetaData().getPrimaryKeys(null, null, tableName);
        SortedSet<String> keys = new TreeSet<>();
        while (pkColumns.next()) {
            String pkColumnName = pkColumns.getString("COLUMN_NAME");
            keys.add(pkColumnName);
        }
        return keys;
    }
     */

    @Override
    protected Stock findStock(String stockName) {
        ResultSet result = query("SELECT * FROM ?", stockName);
        try {
            getConnection().getMetaData().getPrimaryKeys(null, null, stockName);
            final HashMap<PrimaryKey, Supply> suppliers = new HashMap<>();

            // repeat over every row/supply
            while (result.next()) {
                Supply supply = Colossus.getProvider(stockName).deserialize(result);
                supply.setStockName(stockName);
                suppliers.put(supply.getPrimaryKey(), supply);
            }
            return new Stock(stockName, suppliers);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void deleteStock(String stockName) {
        query("DROP TABLE ?", stockName);
    }

    @Override
    protected Supply insertSupply(Supply supply) {
        Map<String, Object> data = supply.serialize();

        List<Object> params = new ArrayList<>();
        params.add(supply.getStockName());
        params.addAll(data.values());

        query("INSERT INTO ? (" + String.join(", ", data.keySet()) + ") VALUES" +
                "(" + "?, ".repeat(data.size()-1) + "?)", params);
        return supply;
    }

    private String getWhereClause(List<String> keys) {
        StringBuilder whereClause = new StringBuilder("WHERE " + keys.get(0) + " = ?");
        if (keys.size() > 1) {
            for (String key : keys) {
                whereClause.append(" AND ").append(key).append(" = ?");
            }
        }
        return whereClause.toString();
    }

    @Override
    public void updateSupply(Supply supply) {
        List<String> modifiedKeys = supply.getModifiedKeys();
        PrimaryKey pk = supply.getPrimaryKey();

        List<Object> params = new ArrayList<>();
        params.add(supply.getStockName());
        Map<String, Object> data = supply.serialize();

        modifiedKeys.forEach(key -> params.add(data.get(key)));
        List<String> keys = new ArrayList<>(pk.keys().keySet());
        keys.forEach(key -> params.add(data.get(key)));

        query("UPDATE ? SET " + String.join(" = ?, ", modifiedKeys) + " = ? " + getWhereClause(keys), params);
    }

    @Override
    protected void deleteSupply(Supply supply) {
        PrimaryKey pk = supply.getPrimaryKey();
        List<String> keys = new ArrayList<>(pk.keys().keySet());

        Map<String, Object> data = supply.serialize();
        List<Object> params = new ArrayList<>();

        params.add(supply.getStockName());
        keys.forEach(key -> params.add(data.get(key)));

        query("DELETE FROM ? " + getWhereClause(keys), params);
    }
}