package net.ryanland.colossus.sys.file.database.provider.json;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.HashMap;

public class JsonMembersProvider extends JsonProvider {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public Object serialize(Supply supply) {
        JsonObject json = new JsonObject();

        // serializers
        json.add("_user_id", serializeElement(supply.get("_user_id")));
        json.add("_guild_id", serializeElement(supply.get("_guild_id")));

        return json;
    }

    @Override
    public Supply deserialize(JsonObject data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.get("_user_id").getAsString());
        values.put("_guild_id", data.get("_guild_id").getAsString());

        return new Supply(values);
    }
}
