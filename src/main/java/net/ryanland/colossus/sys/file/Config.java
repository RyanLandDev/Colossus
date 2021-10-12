package net.ryanland.colossus.sys.file;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class Config {

    private final JsonObject rawConfig;

    private final String token;
    private final String clientId;
    private final String permissions;

    private final String supportGuild;
    private final String testGuild;
    private final boolean testing;

    public Config(@NotNull JsonObject rawConfig) {
        this.rawConfig = rawConfig;

        token = rawConfig.get("token").getAsString();
        clientId = rawConfig.get("client_id").getAsString();
        permissions = rawConfig.get("permissions").getAsString();
        supportGuild = rawConfig.get("support_guild").getAsString();
        testGuild = rawConfig.get("test_guild").getAsString();
        testing = rawConfig.get("testing").getAsBoolean();
    }

    public JsonObject getRawConfig() {
        return rawConfig;
    }

    public String getToken() {
        return token;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getSupportGuildId() {
        return supportGuild;
    }

    public String getTestGuildId() {
        return testGuild;
    }

    public boolean isTesting() {
        return testing;
    }
}
