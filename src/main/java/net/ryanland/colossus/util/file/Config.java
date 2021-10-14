package net.ryanland.colossus.util.file;

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

    public Config(final String token, final String clientId, final String permissions,
                  String supportGuild, final String testGuild, final boolean testing) {
        rawConfig = new JsonObject();

        this.token = token;
        this.clientId = clientId;
        this.permissions = permissions;
        this.supportGuild = supportGuild;
        this.testGuild = testGuild;
        this.testing = testing;
    }

    public Config(final String token, final String clientId, final String testGuild) {
        this(token, clientId, testGuild, true);
    }

    public Config(final String token, final String clientId, final String testGuild, final boolean testing) {
        this(token, clientId, null, null, testGuild, testing);
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
