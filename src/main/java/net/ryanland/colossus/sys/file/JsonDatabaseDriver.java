package net.ryanland.colossus.sys.file;

import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class JsonDatabaseDriver extends DatabaseDriver {
    
    public JsonDatabaseDriver(String databaseDirectory) {
        LocalFile dir = LocalFile.validateDirectoryPath(databaseDirectory);

        LocalFile members = new LocalFileBuilder()
            .setName(databaseDirectory + "/members");

            //todo User, Guild, SelfUser (global)
    }
    
    /**
     * Get all caches for all used client types.
     * <br>You can also create your own implementation of {@link TableCache} by overriding its methods and using it here.
     * <br>Example code:<br><br>
     * <code>return List.of(new TableCache&lt;User&gt;(), new TableCache&lt;SelfUser&gt;());</code>
     */
    @Override
    protected List<TableCache<? extends ISnowflake>> getCaches() {
        return List.of(
            new TableCache<Member>(), new TableCache<User>(),
            new TableCache<Guild>(), new TableCache<SelfUser>() // SelfUser = global
        );
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
     * @param table The table to insert
     * @return The table inserted
     */
    @Override
    protected <T extends ISnowflake> Table<T> insertTable(Table<T> table) {
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
     * @param table The table to update (with)
     */
    @Override
    public <T extends ISnowflake> void updateTable(Table<T> table) {

    }
}
