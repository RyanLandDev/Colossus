package net.ryanland.colossus.sys.oldfile.database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.sys.oldfile.LocalFile;
import net.ryanland.colossus.sys.oldfile.LocalFileBuilder;
import net.ryanland.colossus.sys.oldfile.LocalFileType;
import net.ryanland.colossus.sys.oldfile.serializer.JsonTableSerializer;

import java.io.IOException;

/**
 * JSON implementation of {@link DatabaseDriver}.<br>
 * <b>Warning:</b> Using JSON as a database is very unreliable and is <i>not recommended</i>.
 */
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

    private <T extends ISnowflake> LocalFile getFile(T client) {
        if (client instanceof Member) return members;
        if (client instanceof SelfUser) return global;
        if (client instanceof User) return users;
        if (client instanceof Guild) return guilds;

        throw new IllegalArgumentException();
    }

    private <T extends ISnowflake> JsonObject getJson(T client) {
        try {
            return getFile(client).parseJson();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends ISnowflake> Table<T> findTable(T client) {
        JsonElement element = null;
        if (client instanceof Member)
            element = getJson(client).getAsJsonObject(((Member) client).getGuild().getId()).get(client.getId());
        else if (client instanceof SelfUser || client instanceof User || client instanceof Guild)
            element = getJson(client).get(client.getId());

        if (element == null) return null;
        return (Table<T>) JsonTableSerializer.getInstance().deserialize(element.getAsJsonObject());
    }

    private <T extends ISnowflake> JsonObject updateJson(T client, JsonObject table) {
        JsonObject json = getJson(client);
        if (client instanceof Member) {
            // update member json
            String guildId = ((Member) client).getGuild().getId();
            if (!json.keySet().contains(guildId)) {
                // add guild object if it doesn't exist yet
                json.add(guildId, new JsonObject());
            }
            // update values
            JsonObject guildJson = json.getAsJsonObject(guildId);
            guildJson.remove(client.getId());
            guildJson.add(client.getId(), table);

            json.remove(guildId);
            json.add(guildId, guildJson);
        } else if (client instanceof SelfUser || client instanceof User || client instanceof Guild) {
            // update other types of json
            json.remove(client.getId());
            json.add(client.getId(), table);
        }
        return json;
    }

    @Override
    protected <T extends ISnowflake> Table<T> insertTable(T client, Table<T> table) {
        JsonObject json = updateJson(client, JsonTableSerializer.getInstance().serialize(table));
        getFile(client).write(json);
        return table;
    }

    @Override
    protected <T extends ISnowflake> void deleteTable(T client) {
        JsonObject json = getJson(client);

        if (client instanceof Member) {
            String guildId = ((Member) client).getGuild().getId();
            JsonObject guildJson = json.getAsJsonObject(guildId);
            guildJson.remove(client.getId());
            json.remove(guildId);
            if (!guildJson.keySet().isEmpty()) json.add(guildId, guildJson);
        } else if (client instanceof SelfUser || client instanceof User || client instanceof Guild) {
            json.remove(client.getId());
        }

        getFile(client).write(json);
    }

    @Override
    public <T extends ISnowflake> void updateTable(T client, Table<T> table) {
        JsonObject json = updateJson(client, JsonTableSerializer.getInstance().serialize(table));
        getFile(client).write(json);
    }
}
