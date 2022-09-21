package net.ryanland.colossus.sys.oldfile.database;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;

import java.util.WeakHashMap;

/**
 * A cache holder for {@link Table}s from the database. Uses {@link WeakHashMap}.
 * @param <T> The type of entity this table is for, for example {@link User}
 * @see DatabaseDriver
 */
public class TableCache<T extends ISnowflake> {

    private final WeakHashMap<String, Table<T>> CACHE = new WeakHashMap<>();

    private final Class<T> type;

    public TableCache(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

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
