package net.ryanland.colossus.sys.file.database.mongo;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.Supply;
import org.bson.Document;

import java.util.HashMap;

public class MongoGuildsProvider extends MongoProvider {

    @Override
    public String getStockName() {
        return "guilds";
    }

    @Override
    public Document serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers
        data.put("_guild_id", supply.get("_guild_id"));
        if (supply.get("prefix") != null) data.put("prefix", supply.get("prefix"));

        return new Document(data);
    }

    @Override
    public Supply deserialize(Document data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_guild_id", data.getString("_guild_id"));
        if (data.get("prefix") != null) values.put("prefix", data.getString("prefix"));

        return new Supply(getStockName(), values);
    }
}
