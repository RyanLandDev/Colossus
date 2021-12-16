package net.ryanland.colossus.sys.file.serializer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JsonElementSerializer implements Serializer<Object, JsonElement> {

    private static final JsonElementSerializer instance = new JsonElementSerializer();

    public static JsonElementSerializer getInstance() {
        return instance;
    }

    private final List<Function<JsonElement, Object>> serializers = List.of(
        JsonElement::getAsJsonObject, JsonElement::getAsJsonArray, JsonElement::getAsString,
        JsonElement::getAsInt, JsonElement::getAsBoolean, JsonElement::getAsNumber
    );

    @Override
    public Object serialize(@NotNull JsonElement toSerialize) {
        for (Function<JsonElement, Object> function : serializers) {
            try {
                Object value = function.apply(toSerialize);
                if (value instanceof JsonArray) {
                    List<Object> result = new ArrayList<>();
                    ((JsonArray) value).forEach(e -> result.add(serialize(e)));
                    return result;
                }
                return value;
            } catch (ClassCastException | IllegalStateException | NumberFormatException ignored) {}
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonElement deserialize(@NotNull Object toDeserialize) {
        return new GsonBuilder().create().toJsonTree(toDeserialize);
    }
}
