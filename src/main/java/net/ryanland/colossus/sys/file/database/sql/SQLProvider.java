package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.file.database.ValueProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQLProvider extends Provider<HashMap<String, Object>, ResultSet> {

    @Override
    public final Supply deserialize(ResultSet data) {
        try {
            return deserializeSQL(data);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public abstract Supply deserializeSQL(ResultSet data) throws SQLException;

    protected void processValueProviderSerializations(HashMap<String, Object> data, Supply supply) {
        for (ValueProvider<?, ?, ?> p : Colossus.getSQLDatabaseDriver().getValueProviders(getStockName())) {
            SQLValueProvider<?> provider = (SQLValueProvider<?>) p;
            try { // add column if it does not exist yet
                Colossus.getSQLDatabaseDriver().query("ALTER TABLE " + getStockName() + " ADD " + provider.getKeyName() + " " + provider.getSQLDataType());
            } catch (IllegalArgumentException ignored) {}
            data.put(provider.getKeyName(), provider.serialize(supply.get(provider.getKeyName())));
        }
    }

    protected void processValueProviderDeserializations(HashMap<String, Object> values, ResultSet data) {
        for (ValueProvider<?, ?, ?> p : Colossus.getSQLDatabaseDriver().getValueProviders(getStockName())) {
            SQLValueProvider<?> provider = (SQLValueProvider<?>) p;
            values.put(provider.getKeyName(), provider.deserialize(data));
        }
    }
}
