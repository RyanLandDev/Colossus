package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonGlobalProvider extends JsonProvider {

    @Override
    public String getStockName() {
        return "global";
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject serialize(Supply supply) {
        JsonObject json = new JsonObject();

        // serializers
        json.add("_bot_id", serializeElement(supply.get("_bot_id")));
        json.add("disabled_commands", serializeElement(((List<BasicCommand>) supply.get("disabled_commands", List.of()))
            .stream().map(command -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("command_name", command.getName());
                obj.addProperty("command_type", command.getCommandType().getId());
                return obj;
        }).toArray(JsonObject[]::new)));

        return json;
    }

    @Override
    public Supply deserialize(JsonObject data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_bot_id", data.get("_bot_id").getAsString());
        List<BasicCommand> disabledCommands = new ArrayList<>();
        for (JsonElement obj : data.get("disabled_commands").getAsJsonArray()) {
            String commandName = obj.getAsJsonObject().get("command_name").getAsString();
            CommandType commandType = CommandType.of(obj.getAsJsonObject().get("command_type").getAsInt());
            disabledCommands.add(commandType.getCommand(commandName));
        }
        values.put("disabled_commands", disabledCommands);

        return new Supply(getStockName(), values);
    }
}
