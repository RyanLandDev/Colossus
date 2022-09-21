package net.ryanland.colossus.sys.oldfile.database;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.Colossus;
import org.jetbrains.annotations.Contract;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Different types of tables of data the database has.<br>
 * Examples include: UserTable, MemberTable, GuildTable, GlobalTable, etc.<br>
 * Uses {@link LinkedHashMap} to store deserialized values.
 *
 * <p>Preserved keys: {@code _id _dc _prf}
 * @param <T> The type of entity this table is for, for example {@link User}
 * @see DatabaseDriver
 */
public class Table<T extends ISnowflake> {

    private final LinkedHashMap<String, Object> DATA = new LinkedHashMap<>();

    public Table(String clientId) {
        put("_id", clientId);
    }

    public Table(String clientId, Map<String, Object> deserializedData) {
        DATA.putAll(deserializedData);
        put("_id", clientId);
    }

    /**
     * Get the raw {@link LinkedHashMap} associated with this {@link Table}, containing deserialized values
     */
    public final LinkedHashMap<String, Object> getRawData() {
        return DATA;
    }

    /**
     * Returns the value associated with the provided key, and performs an unchecked cast to it.<br>
     * Will be {@code null} if the value does not exist.
     * @see #get(String, Object) 
     */
    @SuppressWarnings("unchecked")
    public final <R> R get(String key) {
        return (R) DATA.get(key);
    }

    /**
     * Returns the value associated with the provided key, and performs an unchecked cast to it.
     * @param defaultValue If the value is null, this value will be returned instead. See {@link #push(ISnowflake)}.
     * @see #get(String) 
     */
    @SuppressWarnings("unchecked")
    public final <R> R get(String key, Object defaultValue) {
        R value = get(key);
        if (value == null) return (R) defaultValue;
        else return value;
    }

    /**
     * Gets the ID/snowflake of {@link T}
     */
    public final String getId() {
        return get("_id");
    }

    /**
     * Associates the specified value with the specified key in this table.<br>
     * Note: This method will only update this instance, not the database. See {@link #push(ISnowflake)}.
     * @param key The key to update
     * @param value The value to be associated with the provided key
     * @return {@code this}; the updated table
     * @see DatabaseDriver#updateTable(ISnowflake, Table)
     */
    @Contract("_, _ -> this")
    public final Table<T> put(String key, Object value) {
        DATA.put(key, value);
        return this;
    }

    /**
     * Associates the specified value with the specified key in this table, while also providing the old value associated with this key.<br>
     * Note: This method will only update this instance, not the database. See {@link #push(ISnowflake)}.
     * @param key The key to update
     * @param modifier The function; giving the old value and returning the value to be associated with the specified key
     * @return {@code this}; the updated table
     * @see DatabaseDriver#updateTable(ISnowflake, Table)
     */
    @Contract("_, _ -> this")
    public final <V> Table<T> modify(String key, Function<V, V> modifier) {
        return put(key, modifier.apply(get(key)));
    }

    /**
     * Updates the database using the configured {@link DatabaseDriver} with this {@link Table} for the provided client
     */
    public final void push(T client) {
        Colossus.getDatabaseDriver().updateTable(client, this);
    }
}