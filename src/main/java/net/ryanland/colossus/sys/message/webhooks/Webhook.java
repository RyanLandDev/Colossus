package net.ryanland.colossus.sys.message.webhooks;

import com.google.gson.JsonObject;

public class Webhook {

    private final JsonObject rawWebhooks;

    private final String guildTraffic;
    //...

    public Webhook(JsonObject rawWebhooks) {
        this.rawWebhooks = rawWebhooks;

        this.guildTraffic = rawWebhooks.get("guild_traffic").getAsString();
    }

    public JsonObject getRawWebhooks() {
        return this.rawWebhooks;
    }

    public String getGuildTraffic() {
        return this.guildTraffic;
    }
}
