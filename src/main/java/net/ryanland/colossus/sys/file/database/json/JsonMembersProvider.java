package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.HashMap;

public class JsonMembersProvider extends JsonProvider {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public JsonObject serialize(Supply supply) {
        JsonObject json = new JsonObject();

        // serializers
        json.add("_user_id", serializeElement(supply.get("_user_id")));
        json.add("_guild_id", serializeElement(supply.get("_guild_id")));

        processValueProviderSerializations(json, supply);

        return json;
    }

    @Override
    public Supply deserialize(JsonObject data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.get("_user_id").getAsString());
        values.put("_guild_id", data.get("_guild_id").getAsString());

        processValueProviderDeserializations(values, data);

        return new Supply(getStockName(), values);
    }
}
