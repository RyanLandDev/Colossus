package net.ryanland.colossus.util.file.database;

import net.dv8tion.jda.api.entities.User;

import java.util.LinkedHashMap;
import java.util.WeakHashMap;

/**
 * A cache holder for {@link Table}s from the database. Uses {@link WeakHashMap}.
 * @param <T> The type of entity this table is for, for example {@link User}
 */
public class TableCache<T> {

    private final WeakHashMap<String, Table<T>> CACHE = new WeakHashMap<>();

    public Table<T> get(String id) {
        return CACHE.get(id);
    }

    public void put(String id, Table<T> newValue) {
        CACHE.put(id, newValue);
    }

    public void remove(String id) {
        CACHE.remove(id);
    }

}
