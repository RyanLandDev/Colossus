package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLGuildsProvider extends SQLProvider {

    @Override
    public String getStockName() {
        return "guilds";
    }

    @Override
    public HashMap<String, Object> serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers
        data.put("_guild_id", supply.get("_guild_id"));
        if (supply.get("prefix") != null) data.put("prefix", supply.get("prefix"));

        return data;
    }

    @Override
    public Supply deserializeSQL(ResultSet data) throws SQLException {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_guild_id", data.getString("_guild_id"));
        if (data.getString("prefix") != null) values.put("prefix", data.getString("prefix"));

        return new Supply(getStockName(), values);
    }
}
