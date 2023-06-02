package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.command.cooldown.Cooldown;
import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLUsersProvider extends SQLProvider {

    @Override
    public String getStockName() {
        return "users";
    }

    @Override
    public HashMap<String, Object> serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers -----------------------------------
        String userId = supply.get("_user_id");
        data.put("_user_id", userId);
        // cooldowns
        Colossus.getSQLDatabaseDriver().query("DELETE FROM cooldowns WHERE user_id = ?", userId);
        List<Cooldown> cooldowns = supply.get("cooldowns", List.of());
        for (Cooldown cooldown : cooldowns) {
            Supply cooldownSupply = new Supply("cooldowns", Map.of(
                "user_id", userId,
                "command_name", cooldown.command().getName(),
                "command_type", cooldown.command().getCommandType().getId(),
                "expires", cooldown.expires()
            ));
            // insert
            Colossus.getSQLDatabaseDriver().insertSupply(cooldownSupply);
        }
        supply.getModifiedKeys().remove("cooldowns");

        processValueProviderSerializations(data, supply);

        return data;
    }

    @Override
    public Supply deserializeSQL(ResultSet data) throws SQLException {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers ----------------------------------------------
        String userId = data.getString("_user_id");
        values.put("_user_id", userId);
        // cooldowns
        List<Cooldown> cooldowns = new ArrayList<>();
        ResultSet cooldownsData = Colossus.getSQLDatabaseDriver().query("SELECT * FROM cooldowns WHERE user_id = ?", userId);
        while (cooldownsData.next()) {
            // get info
            String commandName = cooldownsData.getString("command_name");
            CommandType commandType = CommandType.of(cooldownsData.getInt("command_type"));
            Date expires = cooldownsData.getDate("expires");

            cooldowns.add(new Cooldown(commandType.getCommand(commandName), expires));
        }
        values.put("cooldowns", cooldowns);

        processValueProviderDeserializations(values, data);

        return new Supply(getStockName(), values);
    }

    public static class CooldownsProvider extends SQLProvider {

        @Override
        public String getStockName() {
            return "cooldowns";
        }

        @Override
        public HashMap<String, Object> serialize(Supply toSerialize) {
            return toSerialize.getValues();
        }

        @Override
        public Supply deserializeSQL(ResultSet data) {
            return null;
        }
    }
}
