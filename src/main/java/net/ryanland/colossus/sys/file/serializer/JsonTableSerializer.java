package net.ryanland.colossus.sys.file.serializer;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.Table;
import org.jetbrains.annotations.NotNull;

public class JsonTableSerializer implements Serializer<JsonObject, Table<?>> {

    private static final JsonTableSerializer instance = new JsonTableSerializer();

    public static JsonTableSerializer getInstance() {
        return instance;
    }

    @Override
    public JsonObject serialize(@NotNull Table<?> toSerialize) {
        JsonObject json = new JsonObject();
        toSerialize.getRawData().forEach((key, value) -> json.add(key, JsonElementSerializer.getInstance().deserialize(value)));
        return json;
    }

    @Override
    public Table<?> deserialize(@NotNull JsonObject toDeserialize) {
        Table<?> table = new Table<>(toDeserialize.get("_id").getAsString());
        toDeserialize.entrySet().forEach(e -> table.put(e.getKey(), JsonElementSerializer.getInstance().serialize(e.getValue())));
        return table;
    }
}
