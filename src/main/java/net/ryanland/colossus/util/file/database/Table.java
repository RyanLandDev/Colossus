package net.ryanland.colossus.util.file.database;

import net.dv8tion.jda.api.entities.User;

import java.util.LinkedHashMap;

/**
 * Different types of tables of data the database has.
 * Examples include: UserTable, MemberTable, GuildTable, GlobalTable, etc.
 * Uses {@link LinkedHashMap} to store values.
 * @param <T> The type of entity this table is for, for example {@link User}
 */
public abstract class Table<T> {

    private final LinkedHashMap<String, Object> DATA = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <R> R get(String key) {
        return (R) DATA.get(key);
    }

    public String getId() {
        return get("id");
    }
}
