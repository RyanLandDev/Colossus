package net.ryanland.colossus.sys.file.database.mongo;

import net.ryanland.colossus.sys.file.database.Supply;
import org.bson.Document;

import java.util.HashMap;

public class MongoMembersProvider extends MongoProvider {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public Document serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers
        data.put("_user_id", supply.get("_user_id"));
        data.put("_guild_id", supply.get("_guild_id"));

        processValueProviderSerializations(data, supply);

        return new Document(data);
    }

    @Override
    public Supply deserialize(Document data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.getString("_user_id"));
        values.put("_guild_id", data.getString("_guild_id"));

        processValueProviderDeserializations(values, data);

        return new Supply(getStockName(), values);
    }
}
