package net.ryanland.colossus.sys.file.database.mongo;

import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.sys.file.database.Supply;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MongoGlobalProvider extends MongoProvider {

    @Override
    public String getStockName() {
        return "global";
    }

    @Override
    public Document serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers -------------------------------------
        data.put("_bot_id", supply.get("_bot_id"));
        // disabled commands
        List<BasicCommand> disabledCommands = supply.get("disabled_commands", List.of());
        data.put("disabled_commands", disabledCommands.stream()
            .map(command -> command.getCommandType().getId() + ";" + command.getName())
            .toList());

        return new Document(data);
    }

    @Override
    public Supply deserialize(Document data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_bot_id", data.getString("_bot_id"));
        // disabled commands
        List<BasicCommand> disabledCommands = data.getList("disabled_commands", String.class, List.of()).stream()
            .map(s -> {
                String[] elements = s.split(";", 2);
                CommandType type = CommandType.of(Integer.parseInt(elements[0]));
                return type.getCommand(elements[1]);
            })
            .collect(Collectors.toList());
        values.put("disabled_commands", disabledCommands);

        return new Supply(getStockName(), values);
    }
}
