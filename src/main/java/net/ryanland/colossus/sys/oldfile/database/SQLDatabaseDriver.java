package net.ryanland.colossus.sys.oldfile.database;

import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.Colossus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * SQL implementation of {@link DatabaseDriver}.<br>
 *
 * <p><b>Important note:</b> SQL tables won't be created for you, the driver will only update them. Create them beforehand.
 * <br>The following tables + columns should exist <i>at least</i>:
 * <ul>
 * <li>{@code global}
 *     <ul>
 *         <li>{@code _id - PK varchar(25)}</li>
 *     </ul></li>
 * <li>{@code guilds}
 *     <ul>
 *         <li>{@code _id - PK varchar(25)}</li>
 *         <li>{@code _prf - varchar(?)} <i>if guild prefixes are used - max length is up to you</i></li>
 *     </ul></li>
 * <li>{@code members}
 *     <ul>
 *         <li>{@code _user_id - PK NN varchar(25)}</li>
 *         <li>{@code _guild_id - PK NN varchar(25)}</li>
 *     </ul></li>
 * <li>{@code users}
 *     <ul>
 *         <li>{@code _id - PK varchar(25)}</li>
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
 * Every snowflake table requires a {@code _id} PK column of type {@code varchar(25)}.
 */
public abstract class SQLDatabaseDriver extends DatabaseDriver {

    public abstract Connection getConnection();

    /**
     * Gets the name of the SQL table associated with the provided client
     */
    protected <T extends ISnowflake> String getTableName(T client) {
        if (client instanceof Member) return "members";
        if (client instanceof SelfUser) return "global";
        if (client instanceof User) return "users";
        if (client instanceof Guild) return "guilds";
        throw new IllegalArgumentException();
    }

    /**
     * Gets the where clause for the provided client
     */
    protected <T extends ISnowflake> String getWhereClause(T client) {
        if (client instanceof SelfUser || client instanceof User || client instanceof Guild) {
            return "_id = " + client.getId();
        } else if (client instanceof Member) {
            return "_user_id = " + client.getId() + " AND _guild_id = " + ((Member) client).getGuild().getId();
        }
        throw new IllegalArgumentException();
    }

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

    /**
     * Performs an SQL query and returns the value of the first column
     */
    @SuppressWarnings("all")
    public final <R> R singleQuery(String query) {
        try {
            return (R) query(query).getObject(1);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected <T extends ISnowflake> Table<T> findTable(T client) {
        String sql = "SELECT * FROM " + getTableName(client) + " WHERE " + getWhereClause(client);
        ResultSet result = query(sql);

        try {
            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            for (int i = 0; i < result.getFetchSize(); i++) {
                String key = result.getMetaData().getColumnName(i);
                Object value = Colossus.getProvider(key).deserialize(result.getObject(i));
                data.put(key, value);
            }
            return new Table<>(client.getId(), data);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected <T extends ISnowflake> Table<T> insertTable(T client, Table<T> table) {
        LinkedHashMap<String, Object> data = table.getRawData();
        String keys = String.join(", ", data.keySet());

        String sql = "INSERT INTO %s(%s) VALUES(%s)"
            .formatted(getTableName(client), keys, "?,".repeat(data.keySet().size()-1) + "?");
        PreparedStatement ps;
        try {
             ps = getConnection().prepareStatement(sql);
            int i = 0;
            for (String key : data.keySet()) {
                i++;
                ps.setObject(i, Colossus.getProvider(key).serialize(data.get(key)));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        query(ps);

        return table;
    }

    @Override
    protected <T extends ISnowflake> void deleteTable(T client) {
        query("DELETE FROM " + getTableName(client) + " WHERE " + getWhereClause(client));
    }

    @Override
    public <T extends ISnowflake> void updateTable(T client, Table<T> table) {
        Table<T> oldTable = findTable(client);
        List<String> updatedKeys = new ArrayList<>();
        table.getRawData().forEach((key, value) -> {
            if (oldTable.get(key) != value) {
                updatedKeys.add(key);
            }
        });

        String query = "UPDATE %s SET %s = ? WHERE %s"
            .formatted(getTableName(client), String.join(" = ?, ", updatedKeys), getWhereClause(client));
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            int i = 0;
            for (String key : updatedKeys) {
                i++;
                ps.setObject(i, Colossus.getProvider(key).serialize(table.get(key)));
            }
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
