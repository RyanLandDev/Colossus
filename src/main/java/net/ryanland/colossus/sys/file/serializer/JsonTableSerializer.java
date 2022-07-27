package net.ryanland.colossus.sys.file.serializer;

import com.google.gson.*;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JsonTableSerializer implements Serializer<JsonObject, Table<?>> {

    private static final JsonTableSerializer instance = new JsonTableSerializer();

    public static JsonTableSerializer getInstance() {
        return instance;
    }

    // (de)serialize json objects ----------------------------------------

    @Override
    public JsonObject serialize(@NotNull Table<?> toSerialize) {
        JsonObject json = new JsonObject();
        toSerialize.getRawData().forEach((key, value) -> json.add(key, serializeElement(key, value)));
        return json;
    }

    @Override
    public Table<?> deserialize(@NotNull JsonObject toDeserialize) {
        Table<?> table = new Table<>(toDeserialize.get("_id").getAsString());
        toDeserialize.entrySet().forEach(e -> table.put(e.getKey(), deserializeElement(e.getKey(), e.getValue())));
        return table;
    }

    // (de)serialize individual json elements -----------------------------

    private static final Gson GSON_BUILDER = new GsonBuilder().create();

    @SuppressWarnings("all")
    public JsonElement serializeElement(String key, Object toSerialzie) {
        return (JsonElement) Colossus.getProvider(key).serialize(GSON_BUILDER.toJsonTree(toSerialzie));
    }

    private final List<Function<JsonElement, Object>> elementSerializers = List.of(
        JsonElement::getAsJsonObject, JsonElement::getAsJsonArray, JsonElement::getAsString,
        JsonElement::getAsInt, JsonElement::getAsBoolean, JsonElement::getAsNumber
    );

    public Object deserializeElement(String key, JsonElement toDeserialize) {
        for (Function<JsonElement, Object> function : elementSerializers) {
            try {
                Object value = function.apply(toDeserialize);
                if (value instanceof JsonArray) {
                    List<Object> result = new ArrayList<>();
                    ((JsonArray) value).forEach(e -> result.add(deserializeElement(key, e)));
                    return Colossus.getProvider(key).deserialize(result);
                }
                return Colossus.getProvider(key).deserialize(value);
            } catch (ClassCastException | IllegalStateException | NumberFormatException ignored) {}
        }
        throw new IllegalStateException();
    }
}
