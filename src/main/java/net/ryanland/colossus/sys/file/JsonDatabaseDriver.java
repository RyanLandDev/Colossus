package net.ryanland.colossus.sys.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.sys.file.serializer.JsonTableSerializer;

import java.io.IOException;
import java.util.List;

public class JsonDatabaseDriver extends DatabaseDriver {

    private final LocalFile dir;
    private final LocalFile members;
    private final LocalFile users;
    private final LocalFile guilds;
    private final LocalFile global;

    public JsonDatabaseDriver(String databaseDirectory) {
        dir = LocalFile.validateDirectoryPath(databaseDirectory);

        members = new LocalFileBuilder()
            .setName(databaseDirectory + "/members")
            .setFileType(LocalFileType.JSON)
            .buildFile();
        users = new LocalFileBuilder()
            .setName(databaseDirectory + "/users")
            .setFileType(LocalFileType.JSON)
            .buildFile();
        guilds = new LocalFileBuilder()
            .setName(databaseDirectory + "/guilds")
            .setFileType(LocalFileType.JSON)
            .buildFile();
        global = new LocalFileBuilder()
            .setName(databaseDirectory + "/global")
            .setFileType(LocalFileType.JSON)
            .buildFile();
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
            new TableCache<>(Member.class), new TableCache<>(User.class),
            new TableCache<>(Guild.class), new TableCache<>(SelfUser.class) // SelfUser = global
        );
    }

    private <T extends ISnowflake> LocalFile getFile(T client) {
        if (client instanceof Member)
            return members;
        if (client instanceof SelfUser)
            return global;
        if (client instanceof User)
            return users;
        if (client instanceof Guild)
            return guilds;
        throw new IllegalArgumentException();
    }

    private <T extends ISnowflake> JsonObject getJson(T client) {
        try {
            return getFile(client).parseJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    /**
     * Retrieves the data associated with the provided client from the database,
     * and then deserializes it to a {@link Table}.
     *
     * @param client The client to get the table of
     * @return The found table, {@code null} if it doesn't exist
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T extends ISnowflake> Table<T> findTable(T client) {
        JsonElement element = getJson(client).get(client.getId());
        if (element == null) return null;
        return (Table<T>) JsonTableSerializer.getInstance().deserialize(element.getAsJsonObject());
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
        JsonObject json = getJson(client);
        json.add(table.getId(), JsonTableSerializer.getInstance().serialize(table));
        getFile(client).write(json);
        return table;
    }

    /**
     * Deletes the table associated with the provided client from the database.
     *
     * @param client The client of the table to delete
     */
    @Override
    protected <T extends ISnowflake> void deleteTable(T client) {
        JsonObject json = getJson(client);
        json.remove(client.getId());
        getFile(client).write(json);
    }

    /**
     * Updates a {@link Table} in the database with modified values.
     *
     * @param client The client this table is associated with
     * @param table The table to update (with)
     */
    @Override
    public <T extends ISnowflake> void updateTable(T client, Table<T> table) {
        JsonObject json = getJson(client);
        json.remove(client.getId());
        json.add(table.getId(), JsonTableSerializer.getInstance().serialize(table));
        getFile(client).write(json);
    }
}
