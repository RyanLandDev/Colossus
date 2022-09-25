package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLGlobalProvider extends SQLProvider {

    @Override
    public String getStockName() {
        return "global";
    }

    @Override
    public HashMap<String, Object> serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers -------------------------------------
        data.put("_bot_id", supply.get("_bot_id"));
        // disabled commands
        Colossus.getSQLDatabaseDriver().query("DELETE FROM disabled_commands");
        List<BasicCommand> disabledCommands = supply.get("disabled_commands", List.of());
        for (BasicCommand command : disabledCommands) {
            Supply commandSupply = new Supply("disabled_commands", Map.of(
                "command_name", command.getName(),
                "command_type", command.getCommandType().getId()
            ));
            // insert
            Colossus.getSQLDatabaseDriver().insertSupply(commandSupply);
        }
        supply.getModifiedKeys().remove("disabled_commands");

        return data;
    }

    @Override
    public Supply deserialize(ResultSet data) {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        try {
            values.put("_bot_id", data.getString("_bot_id"));
            // disabled commands
            List<BasicCommand> disabledCommands = new ArrayList<>();
            ResultSet dcData = Colossus.getSQLDatabaseDriver().query("SELECT * FROM disabled_commands");
            while (dcData.next()) {
                // get info
                String commandName = dcData.getString("command_name");
                CommandType commandType = CommandType.of(dcData.getInt("command_type"));
                disabledCommands.add(commandType.getCommand(commandName));
            }
            values.put("disabled_commands", disabledCommands);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

        return new Supply(getStockName(), values);
    }

    public static class DisabledCommandsProvider extends SQLProvider {

        @Override
        public String getStockName() {
            return "disabled_commands";
        }

        @Override
        public HashMap<String, Object> serialize(Supply toSerialize) {
            return toSerialize.getValues();
        }

        @Override
        public Supply deserialize(ResultSet data) {
            return null;
        }
    }
}
