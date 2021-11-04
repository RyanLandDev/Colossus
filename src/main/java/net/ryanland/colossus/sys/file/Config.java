package net.ryanland.colossus.sys.file;

import com.google.gson.JsonObject;
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

    public Config(final String token, final String clientId,
                  String supportGuild, final String testGuild, final boolean testing) {
        rawConfig = new JsonObject();
        rawConfig.addProperty("token", token);
        rawConfig.addProperty("client_id", clientId);
        rawConfig.addProperty("support_guild", supportGuild);
        rawConfig.addProperty("test_guild", testGuild);
        rawConfig.addProperty("testing", testing);
        updateValueMap();
    }

    public Config(final String token, final String clientId, final String testGuild) {
        this(token, clientId, testGuild, true);
    }

    public Config(final String token, final String clientId,
                  final String testGuild, final boolean testing) {
        this(token, clientId, null, testGuild, testing);
    }

    public JsonObject getRawConfig() {
        return rawConfig;
    }

    @SuppressWarnings("unchecked")
    public <R> R get(String key) {
        return (R) values.get(key);
    }

    public String getString(String key) {
        return get(key);
    }

    public int getInt(String key) {
        return get(key);
    }

    public boolean getBoolean(String key) {
        return get(key);
    }

    public String getToken() {
        return getString("token");
    }

    public String getClientId() {
        return getString("client_id");
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
