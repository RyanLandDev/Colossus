package net.ryanland.colossus.sys.file.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.local.LocalFile;
import net.ryanland.colossus.sys.file.local.LocalFileBuilder;
import net.ryanland.colossus.sys.file.local.LocalFileType;
import net.ryanland.colossus.sys.file.database.json.JsonProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonConfig implements Config {

    private String filePath;

    public JsonConfig(String filePath) {
        this.filePath = filePath;
        if (!filePath.endsWith(".json")) throw new UnsupportedOperationException("Path must lead to a .json file");
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
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> JsonProvider.serializeElement(entry.getValue()))));
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
    public Boolean getBoolean(String key) {
        if (get(key) == null) return null;
        return get(key).getAsBoolean();
    }
}
