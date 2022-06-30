package net.ryanland.colossus.sys.file.serializer;

import net.ryanland.colossus.sys.file.database.Table;
import org.jetbrains.annotations.NotNull;
import org.bson.Document;

public class MongoTableSerializer implements Serializer<Document, Table<?>> {

    private static final MongoTableSerializer instance = new MongoTableSerializer();

    public static MongoTableSerializer getInstance() {
        return instance;
    }

    @Override
    public Document serialize(@NotNull Table<?> toSerialize) {
        return new Document(toSerialize.getRawData());
    }

    @Override
    public Table<?> deserialize(@NotNull Document toDeserialize) {
        return new Table<>(toDeserialize.getString("_id"), toDeserialize);
    }
}
