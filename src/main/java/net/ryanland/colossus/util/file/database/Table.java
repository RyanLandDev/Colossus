package net.ryanland.colossus.util.file.database;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;

import java.util.LinkedHashMap;

/**
 * Different types of tables of data the database has.
 * Examples include: UserTable, MemberTable, GuildTable, GlobalTable, etc.
 * Uses {@link LinkedHashMap} to store values.
 * @param <T> The type of entity this table is for, for example {@link User}
 */
public class Table<T extends ISnowflake> {

    private final LinkedHashMap<String, Object> DATA = new LinkedHashMap<>();

    /**
     * Get the raw {@link LinkedHashMap} associated with this {@link Table}
     */
    public final LinkedHashMap<String, Object> getRawData() {
        return DATA;
    }

    /**
     * Returns the value associated with the provided key, and performs an unchecked cast to it.
     */
    @SuppressWarnings("unchecked")
    public final <R> R get(String key) {
        return (R) DATA.get(key);
    }

    /**
     * Gets the ID/snowflake of {@link T}
     */
    public final String getId() {
        return get("id");
    }

    /**
     * Associates the provided key with the provided value in this table.<br>
     * Note: This method will only update this instance, not the database.
     * @param key The key to update
     * @param value The value to be associated with the provided key
     * @return {@code this}; the updated table
     * @see DatabaseDriver#update//TODO
     */
    public final Table<T> put(String key, Object value) {
        DATA.put(key, value);
        return this;
    }
}
