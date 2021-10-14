package net.ryanland.colossus.util.file.database;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.Colossus;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public abstract class DatabaseDriver {

    /**
     * Get all caches for all used client types.
     * <br>You can also create your own implementation of {@link TableCache} by overriding its methods and using it here.
     * <br>Example code:<br><br>
     * <code>return new {@link TableCache}&lt;? extends {@link ISnowflake}&gt;[]{
     * <br>&nbsp;&nbsp;&nbsp;&nbsp;new TableCache&lt;User&gt;
     * <br>};</code>
     */
    protected abstract TableCache<? extends ISnowflake>[] getCaches();

    /**
     * Get all
     */
    protected abstract Class<Table<? extends ISnowflake>>[] getTableTypes();

    @SuppressWarnings("unchecked")
    private <T extends ISnowflake> TableCache<T> getCache(T value) {
        for (TableCache<? extends ISnowflake> cache : getCaches()) {
            Colossus.getLogger().debug(cache.getClass().getTypeParameters()[0].getClass().getName() + "\n" +
                value.getClass().getTypeParameters()[0].getClass().getName());

            if (cache.getClass().getTypeParameters()[0].getClass()
                .equals(value.getClass().getTypeParameters()[0].getClass()))
                return (TableCache<T>) cache;
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    private <T extends ISnowflake> Table<T> getEmptyTable(T client) {
        for (Class<Table<? extends ISnowflake>> tableType : getTableTypes()) {
            Colossus.getLogger().debug(tableType.getTypeParameters()[0].getClass().getName() + "\n" +
                client.getClass().getTypeParameters()[0].getClass().getName());

            if (tableType.getTypeParameters()[0].getClass()
                .equals(client.getClass().getTypeParameters()[0].getClass())) {
                try {
                    return (Table<T>) tableType.getDeclaredConstructor().newInstance()
                        .put("id", client.getId());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public <T extends ISnowflake> Table<T> get(T client) {
        return get(client, false);
    }

    public <T extends ISnowflake> Table<T> get(T client, boolean nullIfMissing) {
        return nullOr(getFromCache(client), () -> retrieve(client, nullIfMissing));
    }

    public <T extends ISnowflake> Table<T> getFromCache(T client) {
        return getCache(client).get(client.getId());
    }

    public <T extends ISnowflake> Table<T> retrieve(T client, boolean nullIfMissing) {
        if (nullIfMissing)
            return find(client);
        else
            return nullOr(find(client), () -> insert(client));
    }

    public <T extends ISnowflake> Table<T> find(T client) {
        Table<T> table = findTable(client);
        cache(client, table);
        return table;
    }

    /**
     * Retrieves the data associated with the provided client from the database,
     * and then deserializes it to a {@link Table}.
     * @param client The client to get the table of
     * @param <T> The type of client, e.g. {@link User}
     * @return The found table, {@code null} if it doesn't exist
     */
    protected abstract <T extends ISnowflake> Table<T> findTable(T client);

    public <T extends ISnowflake> Table<T> insert(T client) {
        Table<T> table = insertTable(getEmptyTable(client));
        cache(client, table);
        return table;
    }

    /**
     * Insert a new table in the database.
     * @param table The table to insert
     * @param <T> The type of client, e.g. {@link User}
     * @return The table inserted
     */
    protected abstract <T extends ISnowflake> Table<T> insertTable(Table<T> table);

    public <T extends ISnowflake> void delete(T client) {
        deleteTable(client);
        removeFromCache(client);
    }

    /**
     * Deletes the table associated with the provided client from the database.
     * @param client The client of the table to delete
     * @param <T> The type of client, e.g. {@link User}
     */
    protected abstract <T extends ISnowflake> void deleteTable(T client);

    /**
     * Updates a table in the database with modified values.
     * @param table The table to update (with)
     * @param <T> The type of client, e.g. {@link User}
     */
    protected abstract <T extends ISnowflake> void updateTable(Table<T> table);

    public <T extends ISnowflake> void cache(T client, Table<T> table) {
        getCache(client).put(client.getId(), table);
    }

    public <T extends ISnowflake> void removeFromCache(T client) {
        getCache(client).remove(client.getId());
    }

    /**
     * Alternative to {@link Objects#requireNonNullElse(Object, Object)} with {@link Supplier}
     */
    private static <T> T nullOr(T object, Supplier<T> compareTo) {
        return object != null ? object : compareTo.get();
    }

}
