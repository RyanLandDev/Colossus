package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.*;
import net.ryanland.colossus.sys.file.database.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class JsonProvider extends Provider<JsonObject, JsonObject> {

    private static final Gson GSON_BUILDER = new GsonBuilder().create();

    /**
     * Serializes a value to its {@link JsonElement} representation. Must be of a supported type.
     */
    public static JsonElement serializeElement(Object toSerialize) {
        return GSON_BUILDER.toJsonTree(toSerialize);
    }

    private static final List<Function<JsonElement, Object>> elementDeserializers = List.of(
        JsonElement::getAsJsonObject, JsonElement::getAsJsonArray, JsonElement::getAsInt,
        JsonElement::getAsBoolean, JsonElement::getAsNumber, JsonElement::getAsString
    );

    /**
     * Deserializes a {@link JsonElement} to its native representation
     */
    public static Object deserializeElement(JsonElement toDeserialize) {
        if (toDeserialize.isJsonObject()) return toDeserialize.getAsJsonObject();
        if (toDeserialize.isJsonArray()) {
            List<Object> result = new ArrayList<>();
            toDeserialize.getAsJsonArray().forEach(e -> result.add(deserializeElement(e)));
            return result;
        }
        if (toDeserialize.isJsonPrimitive()) {
            JsonPrimitive value = toDeserialize.getAsJsonPrimitive();
            if (value.isBoolean()) return value.getAsBoolean();
            if (value.isNumber()) return value.getAsNumber();
            if (value.isString()) return value.getAsString();
        }
        return null;
    }
}
