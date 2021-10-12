package net.ryanland.colossus.sys.message.webhooks;

import com.google.gson.JsonObject;

import java.io.IOException;

public class WebhookHandler {

    private static Webhook WEBHOOKS;

    static {
        try {
            JsonObject rawJson = LocalFiles.WEBHOOKS.parseJson();
            WEBHOOKS = new Webhook(rawJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Webhook getWebhooks() {
        return WEBHOOKS;
    }

}
