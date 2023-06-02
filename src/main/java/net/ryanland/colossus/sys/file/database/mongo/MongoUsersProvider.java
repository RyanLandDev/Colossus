package net.ryanland.colossus.sys.file.database.mongo;

import com.google.gson.JsonObject;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.command.cooldown.Cooldown;
import net.ryanland.colossus.sys.file.database.Supply;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MongoUsersProvider extends MongoProvider {

    @Override
    public String getStockName() {
        return "users";
    }

    @Override
    public Document serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers
        data.put("_user_id", supply.get("_user_id"));
        data.put("cooldowns", ((List<Cooldown>) supply.get("cooldowns", List.of()))
            .stream()
            .map(cooldown -> Arrays.asList(cooldown.command().getName(), cooldown.expires(), cooldown.command().getCommandType().getId()))
            .collect(Collectors.toList()));

        processValueProviderSerializations(data, supply);

        return new Document(data);
    }

    @Override
    public Supply deserialize(Document data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.getString("_user_id"));
        values.put("cooldowns", data.getList("cooldowns", List.class, List.of()).stream()
            .map(list -> new Cooldown(CommandType.of((int) list.get(2)).getCommand((String) list.get(0)), (Date) list.get(1)))
            .toList());

        processValueProviderDeserializations(values, data);

        return new Supply(getStockName(), values);
    }
}
