package net.ryanland.colossus.sys.file.database.provider.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.command.cooldown.Cooldown;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JsonUsersProvider extends JsonProvider {

    @Override
    public String getStockName() {
        return "users";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object serialize(Supply supply) {
        JsonObject json = new JsonObject();

        // serializers
        json.add("_user_id", serializeElement(supply.get("_user_id")));
        json.add("cooldowns", serializeElement(((List<Cooldown>) supply.get("cooldowns", List.of()))
            .stream().map(cooldown -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("command_name", cooldown.command().getName());
                obj.addProperty("command_type", cooldown.command().getCommandType().getId());
                obj.addProperty("expires", String.valueOf(cooldown.expires().getTime()));
                return obj;
            }).toArray(JsonObject[]::new)));

        return json;
    }

    @Override
    public Supply deserialize(JsonObject data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.get("_user_id").getAsString());
        List<Cooldown> cooldowns = new ArrayList<>();
        for (JsonElement element : data.get("cooldowns").getAsJsonArray()) {
            JsonObject obj = (JsonObject) element;

            // get info
            String commandName = obj.get("command_name").getAsString();
            CommandType commandType = CommandType.of(obj.get("command_type").getAsInt());
            Date expires = new Date(Long.parseLong(obj.get("expires").getAsString()));

            cooldowns.add(new Cooldown(commandType.getCommand(commandName), expires));
        }
        values.put("cooldowns", cooldowns);

        return new Supply(getStockName(), values);
    }
}
