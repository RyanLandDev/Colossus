package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.*;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.file.database.ValueProvider;
import net.ryanland.colossus.sys.file.database.sql.SQLProvider;
import net.ryanland.colossus.sys.file.database.sql.SQLValueProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public abstract class JsonProvider extends Provider<JsonObject, JsonObject> {

    private static final Gson GSON_BUILDER = new GsonBuilder().create();

    /**
     * Returns an anonymous {@link JsonProvider} that uses solely {@link ValueProvider}s
     */
    public static JsonProvider of(String stockName) {
        return new JsonProvider() {
            @Override
            public String getStockName() {
                return stockName;
            }

            @Override
            public JsonObject serialize(Supply toSerialize) {
                JsonObject json = new JsonObject();
                processValueProviderSerializations(json, toSerialize);
                return json;
            }

            @Override
            public Supply deserialize(JsonObject data) {
                HashMap<String, Object> values = new HashMap<>();
                processValueProviderDeserializations(values, data);
                return new Supply(getStockName(), values);
            }

        };
    }

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

    protected void processValueProviderSerializations(JsonObject json, Supply supply) {
        for (ValueProvider<?, ?, ?> p : Colossus.getDatabaseDriver().getValueProviders(getStockName())) {
            JsonValueProvider<?> provider = (JsonValueProvider<?>) p;
            json.add(provider.getKeyName(), provider.serialize(supply.get(provider.getKeyName())));
        }
    }

    protected void processValueProviderDeserializations(HashMap<String, Object> values, JsonObject data) {
        for (ValueProvider<?, ?, ?> p : Colossus.getDatabaseDriver().getValueProviders(getStockName())) {
            JsonValueProvider<?> provider = (JsonValueProvider<?>) p;
            values.put(provider.getKeyName(), provider.deserialize(data));
        }
    }
}
