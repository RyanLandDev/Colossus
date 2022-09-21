package net.ryanland.colossus.sys.file.database.provider.json;

import com.google.gson.*;
import net.ryanland.colossus.sys.file.database.provider.Provider;

public abstract class JsonProvider extends Provider<JsonObject> {

    private static final Gson GSON_BUILDER = new GsonBuilder().create();

    /**
     * Serializes a value to its {@link JsonElement} representation. Must be of a supported type.
     */
    public static JsonElement serializeElement(Object toSerialize) {
        return GSON_BUILDER.toJsonTree(toSerialize);
    }

    /*
    private static final List<Function<JsonElement, Object>> elementDeserializers = List.of(
        JsonElement::getAsJsonObject, JsonElement::getAsJsonArray, JsonElement::getAsString,
        JsonElement::getAsInt, JsonElement::getAsBoolean, JsonElement::getAsNumber
    );

    /**
     * Deserializes a {@link JsonElement} to its native representation
     *\/
    public static Object deserializeElement(JsonElement toDeserialize) {
        for (Function<JsonElement, Object> function : elementDeserializers) {
            try {
                Object value = function.apply(toDeserialize);
                if (value instanceof JsonArray) {
                    List<Object> result = new ArrayList<>();
                    ((JsonArray) value).forEach(e -> result.add(deserializeElement(e)));
                    return result;
                }
                return value;
                // on exception, try next deserializer
            } catch (ClassCastException | IllegalStateException | NumberFormatException ignored) {}
        }
        throw new IllegalStateException();
    }
    */
}
