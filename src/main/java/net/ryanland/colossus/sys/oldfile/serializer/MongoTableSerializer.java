package net.ryanland.colossus.sys.oldfile.serializer;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.oldfile.database.Table;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class MongoTableSerializer implements Serializer<Document, Table<?>> {

    private static final MongoTableSerializer instance = new MongoTableSerializer();

    public static MongoTableSerializer getInstance() {
        return instance;
    }

    @Override
    public Document serialize(@NotNull Table<?> toSerialize) {
        LinkedHashMap<String, Object> newData = new LinkedHashMap<>();
        toSerialize.getRawData().forEach((key, value) -> newData.put(key, Colossus.getProvider(key).serialize(value)));
        return new Document(newData);
    }

    @Override
    public Table<?> deserialize(@NotNull Document toDeserialize) {
        LinkedHashMap<String, Object> newData = new LinkedHashMap<>();
        toDeserialize.forEach((key, value) -> newData.put(key, Colossus.getProvider(key).deserialize(value)));
        return new Table<>(toDeserialize.getString("_id"), newData);
    }
}