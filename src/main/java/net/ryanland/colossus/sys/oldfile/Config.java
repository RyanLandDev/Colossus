package net.ryanland.colossus.sys.oldfile;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    private final JsonObject rawConfig;

    private final Map<String, Object> values = new LinkedMap<>();

    private void updateValueMap() {
        values.putAll(rawConfig.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public Config(@NotNull JsonObject rawConfig) {
        this.rawConfig = rawConfig;
        updateValueMap();
    }

    public Config(String token, String clientId, String prefix,
                  String supportGuild, String testGuild, boolean testing) {
        rawConfig = new JsonObject();
        rawConfig.addProperty("token", token);
        rawConfig.addProperty("client_id", clientId);
        rawConfig.addProperty("prefix", prefix);
        rawConfig.addProperty("support_guild", supportGuild);
        rawConfig.addProperty("test_guild", testGuild);
        rawConfig.addProperty("testing", testing);
        updateValueMap();
    }

    public JsonObject getRawConfig() {
        return rawConfig;
    }

    public JsonPrimitive get(String key) {
        return (JsonPrimitive) values.get(key);
    }

    public String getString(String key) {
        return get(key).getAsString();
    }

    public int getInt(String key) {
        return get(key).getAsInt();
    }

    public boolean getBoolean(String key) {
        return get(key).getAsBoolean();
    }

    public String getToken() {
        return getString("token");
    }

    public String getClientId() {
        return getString("client_id");
    }

    public String getPrefix() {
        return getString("prefix");
     }

    public String getSupportGuildId() {
        return getString("support_guild");
    }

    public String getTestGuildId() {
        return getString("test_guild");
    }

    public boolean isTesting() {
        return getBoolean("testing");
    }
}
