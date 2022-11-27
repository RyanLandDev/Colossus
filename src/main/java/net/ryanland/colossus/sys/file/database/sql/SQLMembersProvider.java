package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.sys.file.database.Supply;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLMembersProvider extends SQLProvider {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public HashMap<String, Object> serialize(Supply supply) {
        HashMap<String, Object> data = new HashMap<>();

        // serializers
        data.put("_user_id", supply.get("_user_id"));
        data.put("_guild_id", supply.get("_guild_id"));

        return data;
    }

    @Override
    public Supply deserializeSQL(ResultSet data) throws SQLException {
        HashMap<String, Object> values = new HashMap<>();

        // deserializers
        values.put("_user_id", data.getString("_user_id"));
        values.put("_guild_id", data.getString("_guild_id"));

        return new Supply(getStockName(), values);
    }
}
