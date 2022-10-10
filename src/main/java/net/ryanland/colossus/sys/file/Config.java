package net.ryanland.colossus.sys.file;

import com.google.gson.JsonPrimitive;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.Map;

public class Config {

    private final Map<String, JsonPrimitive> values = new LinkedMap<>();

    public Config(Map<String, JsonPrimitive> values) {
        this.values.putAll(values);
    }

    public JsonPrimitive get(String key) {
        return values.get(key);
    }

    public String getString(String key) {
        return get(key).getAsString();
    }

    public Integer getInt(String key) {
        return get(key).getAsInt();
    }

    public Boolean getBoolean(String key) {
        return get(key).getAsBoolean();
    }
}
