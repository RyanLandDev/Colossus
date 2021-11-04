package net.ryanland.colossus.sys.file;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstract class for creating database drivers.
 * <br>You can find a list of pre-made examples here: TODO
 * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
 */
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

    @SuppressWarnings("unchecked")
    private <T extends ISnowflake> TableCache<T> getCache(T value) {
        for (TableCache<? extends ISnowflake> cache : getCaches()) {
            Colossus.getLogger().debug(cache.getClass().getTypeParameters()[0].getClass().getName() + "\n" +
                value.getClass().getTypeParameters()[0].getClass().getName());

            if (cache.getClass().getTypeParameters()[0].getClass()
                .equals(value.getClass().getTypeParameters()[0].getClass()))
                return (TableCache<T>) cache;
        }
        throw new IllegalArgumentException("A cache with the type " + value.getClass().getName() + " does not exist.\n" +
            "You can add it in the getCaches() method of your DatabaseDriver implementation.");
    }

    @SuppressWarnings("unchecked")
    private <T extends ISnowflake> Table<T> getEmptyTable(T client) {
        try {
            return (Table<T>) Table.class.getDeclaredConstructor().newInstance()
                .put("_id", client.getId());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException();
    }

    /**
     * Gets the {@link Table} associated with the provided client.<br>
     * Firstly attempts getting the table from the local cache for this client.
     * If this is null, tries retrieving it from the database. If the table was not found in the database,
     * a new table will be inserted in the database and this table will be returned.
     * @param client The client to get the table of
     * @param <T> The type of client, e.g. {@link User}
     * @return The result table. This will never be null.
     * @see #getFromCache(ISnowflake)
     * @see #retrieve(ISnowflake) 
     * @see #find(ISnowflake) 
     * @see #insert(ISnowflake) 
     * @see #cache(ISnowflake, Table) 
     */
    public <T extends ISnowflake> Table<T> get(T client) {
        return nullOr(getFromCache(client), () -> retrieve(client));
    }

    /**
     * Gets the {@link Table} associated with the provided client from the local cache for this client.
     * @param client The client to get the table of
     * @param <T> The type of client, e.g. {@link User}
     * @return The found table, null if no table for this client is currently in the cache
     * @see #removeFromCache(ISnowflake) 
     */
    public <T extends ISnowflake> Table<T> getFromCache(T client) {
        return getCache(client).get(client.getId());
    }

    /**
     * Retrieves the {@link Table} associated with the provided client from the database
     * and adds it to the local cache for this client,
     * IF the retrieved table is not null. If null, a new table will be inserted in the
     * database and this table will be returned.
     * <br>If you'd like the table to be null if it does not currently exist in the database,
     * please use the method {@link #find(ISnowflake)} instead.
     * @param client The client to get the table of
     * @param <T> The type of client, e.g. {@link User}
     * @return The found table, or the newly inserted table in case the found table was null
     * @see #find(ISnowflake) 
     * @see #insert(ISnowflake) 
     * @see #cache(ISnowflake, Table) 
     */
    public <T extends ISnowflake> Table<T> retrieve(T client) {
        return nullOr(find(client), () -> insert(client));
    }

    /**
     * Retrieves the {@link Table} associated with the provided client from the database
     * and adds it to the local cache for this client.
     * @param client The client to get the table of
     * @param <T> The type of client, e.g. {@link User}
     * @return The found table, {@code null} if it doesn't exist
     * @see #cache(ISnowflake, Table) 
     */
    public <T extends ISnowflake> Table<T> find(T client) {
        Table<T> table = findTable(client);
        if (table == null) return null;
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

    /**
     * Inserts a new table in the database and adds it to the local cache for this client.
     * @param client The client of the table to insert
     * @param <T> The type of client, e.g. {@link User}
     * @return The table inserted
     * @see #cache(ISnowflake, Table) 
     */
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

    /**
     * Deletes the table associated with the provided client from the database, and the local cache for this client.
     * @param client The client of the table to delete
     * @param <T> The type of client, e.g. {@link User}
     * @see #removeFromCache(ISnowflake)
     */
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
     * <br>Provides the old value to help with modification.
     * @param client The client of the table to modify
     * @param tableModifier The table modifier function; providing the old value and returning the new value
     * @param <T> The type of client, e.g. {@link User}
     * @see #updateTable(Table) 
     */
    public <T extends ISnowflake> void modifyTable(T client, Function<Table<T>, Table<T>> tableModifier) {
        updateTable(tableModifier.apply(get(client)));
    }
    
    /**
     * Updates a {@link Table} in the database with modified values.
     * @param table The table to update (with)
     * @param <T> The type of client, e.g. {@link User}
     */
    public abstract <T extends ISnowflake> void updateTable(Table<T> table);

    /**
     * Adds a {@link Table} to the local cache for this client.
     * @param client The client of the table to remove
     * @param table The not-null table to cache
     * @param <T> The type of client, e.g. {@link User}
     * @see #getCaches()
     */
    public <T extends ISnowflake> void cache(T client, @NotNull Table<T> table) {
        getCache(client).put(client.getId(), table);
    }

    /**
     * Removes a table from the local cache for this client.
     * @param client The client of the table to remove
     * @param <T> The type of client, e.g. {@link User}
     * @see #getCaches()
     */
    public <T extends ISnowflake> void removeFromCache(T client) {
        getCache(client).remove(client.getId());
    }

    /**
     * Alternative to {@link Objects#requireNonNullElse(Object, Object)} with the use of a {@link Supplier},
     * allowing the second value's code to be executed only if the first value is null.
     */
    private static <T> T nullOr(T object, Supplier<T> compareTo) {
        return object != null ? object : compareTo.get();
    }

}
