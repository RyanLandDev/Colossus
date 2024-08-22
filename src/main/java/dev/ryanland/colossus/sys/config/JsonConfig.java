package dev.ryanland.colossus.sys.config;

import com.google.gson.*;
import dev.ryanland.colossus.sys.file.LocalFile;
import dev.ryanland.colossus.sys.file.LocalFileBuilder;
import dev.ryanland.colossus.sys.file.LocalFileType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonConfig implements ConfigSupplier {

    private String filePath;

    public JsonConfig(String filePath) {
        this.filePath = filePath;
        if (filePath != null && !filePath.endsWith(".json")) throw new UnsupportedOperationException("Path must lead to a .json file");
    }

    @Override
    public void read() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Get the config file (+ create it if it doesn't exist yet)
        LocalFile configFile = new LocalFileBuilder()
                .setName(filePath.substring(0, filePath.length()-5))// substring removes .json suffix
                .setFileType(LocalFileType.JSON)
                .buildFile();

        // Parse the config file's JSON
        JsonObject configJson = configFile.parseJson();

        LinkedHashMap<String, Object> entries = new LinkedHashMap<>(values); // set entries to default entries
        LinkedHashMap<String, Object> currentEntries = LocalFile.mapOfJson(configJson);
        entries.putAll(currentEntries); // put all current entries and thus overwrite default entries

        configJson = LocalFile.jsonOfEntries(entries);
        configFile.write(gson.toJson(configJson));

        values.clear();
        values.putAll(entries.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new GsonBuilder().create().toJsonTree(entry.getValue()))));
    }

    @Override
    public JsonElement get(String key) {
        return (JsonElement) values.get(key);
    }

    @Override
    public String getString(String key) {
        if (get(key) == null) return null;
        return get(key).getAsString();
    }

    @Override
    public Integer getInt(String key) {
        if (get(key) == null) return null;
        return get(key).getAsInt();
    }

    @Override
    public Long getLong(String key) {
        if (get(key) == null) return null;
        return get(key).getAsLong();
    }

    @Override
    public Float getFloat(String key) {
        if (get(key) == null) return null;
        return get(key).getAsFloat();
    }

    @Override
    public Double getDouble(String key) {
        if (get(key) == null) return null;
        return get(key).getAsDouble();
    }

    @Override
    public Boolean getBoolean(String key) {
        if (get(key) == null) return null;
        return get(key).getAsBoolean();
    }

    @Override
    public <R> List<R> getList(String key) {
        if (get(key) == null) return null;
        return (List<R>) deserializeElement(get(key).getAsJsonArray());
    }

    /**
     * Deserializes a {@link JsonElement} to its native representation
     */
    private static Object deserializeElement(JsonElement toDeserialize) {
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
