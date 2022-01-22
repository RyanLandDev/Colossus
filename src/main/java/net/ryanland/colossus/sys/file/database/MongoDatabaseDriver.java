package net.ryanland.colossus.sys.file.database;

import net.dv8tion.jda.api.entities.ISnowflake;

import java.util.List;

public class MongoDatabaseDriver extends DatabaseDriver {
    //TODO
    public MongoDatabaseDriver() {

    }

    /**
     * Get all caches for all used client types.
     * <br>You can also create your own implementation of {@link TableCache} by overriding its methods and using it here.
     * <br>Example code:<br><br>
     * <code>return List.of(new TableCache&lt;User&gt;, new TableCache&lt;SelfUser&gt;);</code>
     */
    @Override
    protected List<TableCache<? extends ISnowflake>> getCaches() {
        return null;
    }

    /**
     * Retrieves the data associated with the provided client from the database,
     * and then deserializes it to a {@link Table}.
     *
     * @param client The client to get the table of
     * @return The found table, {@code null} if it doesn't exist
     */
    @Override
    protected <T extends ISnowflake> Table<T> findTable(T client) {
        return null;
    }

    /**
     * Insert a new table in the database.
     *
     * @param client The client this table is associated with
     * @param table  The table to insert
     * @return The table inserted
     */
    @Override
    protected <T extends ISnowflake> Table<T> insertTable(T client, Table<T> table) {
        return null;
    }

    /**
     * Deletes the table associated with the provided client from the database.
     *
     * @param client The client of the table to delete
     */
    @Override
    protected <T extends ISnowflake> void deleteTable(T client) {

    }

    /**
     * Updates a {@link Table} in the database with modified values.
     *
     * @param client The client this table is associated with
     * @param table  The table to update (with)
     */
    @Override
    public <T extends ISnowflake> void updateTable(T client, Table<T> table) {

    }
}
