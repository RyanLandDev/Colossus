package net.ryanland.colossus.sys.file.database.provider.json;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.HashMap;

public class JsonGuildsProvider extends JsonProvider {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public Object serialize(Supply supply) {
        JsonObject json = new JsonObject();

        // serializers
        json.add("_guild_id", serializeElement(supply.get("_guild_id")));
        if (supply.get("prefix") != null) json.add("prefix", serializeElement(supply.get("prefix")));

        return json;
    }

    @Override
    public Supply deserialize(JsonObject data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_guild_id", data.get("_guild_id").getAsString());
        if (data.get("prefix") != null) values.put("prefix", data.get("prefix").getAsString());

        return new Supply(values);
    }
}
