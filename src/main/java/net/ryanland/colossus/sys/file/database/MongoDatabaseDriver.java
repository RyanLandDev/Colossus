package net.ryanland.colossus.sys.file.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.sys.file.serializer.MongoTableSerializer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoDatabaseDriver extends DatabaseDriver {

    private final String SECRET;
    private final MongoClient CLIENT;
    private final MongoDatabase DATABASE;

    private final MongoCollection<Document> MEMBER_COLLECTION;
    private final MongoCollection<Document> USER_COLLECTION;
    private final MongoCollection<Document> GUILD_COLLECTION;
    private final MongoCollection<Document> GLOBAL_COLLECTION;

    public MongoDatabaseDriver(String secret, String databaseName) {
        SECRET = secret;
        CLIENT = MongoClients.create(SECRET);
        DATABASE = CLIENT.getDatabase(databaseName);

        MEMBER_COLLECTION = DATABASE.getCollection("members");
        USER_COLLECTION = DATABASE.getCollection("users");
        GUILD_COLLECTION = DATABASE.getCollection("guilds");
        GLOBAL_COLLECTION = DATABASE.getCollection("global");
    }

    @Override
    protected List<TableCache<? extends ISnowflake>> getCaches() {
        return List.of(
            new TableCache<>(Member.class), new TableCache<>(User.class),
            new TableCache<>(Guild.class), new TableCache<>(SelfUser.class) // SelfUser = global
        );
    }

    private <T extends ISnowflake> MongoCollection<Document> getCollection(T client) {
        if (client instanceof Member)
            return MEMBER_COLLECTION;
        if (client instanceof SelfUser)
            return GLOBAL_COLLECTION;
        if (client instanceof User)
            return USER_COLLECTION;
        if (client instanceof Guild)
            return GUILD_COLLECTION;
        throw new IllegalArgumentException();
    }

    @Override
    @SuppressWarnings("all")
    protected <T extends ISnowflake> Table<T> findTable(T client) {
        Document document = getCollection(client).find(Filters.eq("_id", client.getId())).first();
        if (document == null) return null;
        return (Table<T>) MongoTableSerializer.getInstance().deserialize(document);
    }

    @Override
    protected <T extends ISnowflake> Table<T> insertTable(T client, Table<T> table) {
        Document document = MongoTableSerializer.getInstance().serialize(table);
        getCollection(client).insertOne(document);
        return table;
    }

    @Override
    protected <T extends ISnowflake> void deleteTable(T client) {
        getCollection(client).deleteOne(Filters.eq("_id", client.getId()));
    }

    @Override
    public <T extends ISnowflake> void updateTable(T client, Table<T> table) {
        getCollection(client).updateOne(Filters.eq("_id", client.getId()), getUpdates(findTable(client), table));
    }

    private <T extends ISnowflake> List<Bson> getUpdates(Table<T> oldTable, Table<T> newTable) {
        List<Bson> updates = new ArrayList<>();
        // repeat over every value in the new table, if it's not equal to the value in the old table, add an update
        newTable.getRawData().forEach((key, value) -> {
            if (oldTable.getRawData().get(key) != value) {
                updates.add(Updates.set(key, value));
            }
        });
        return updates;
    }
}
