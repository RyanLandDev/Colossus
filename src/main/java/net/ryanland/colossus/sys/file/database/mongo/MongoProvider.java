package net.ryanland.colossus.sys.file.database.mongo;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.file.database.ValueProvider;
import net.ryanland.colossus.sys.file.database.sql.SQLValueProvider;
import org.bson.Document;

import java.sql.ResultSet;
import java.util.HashMap;

public abstract class MongoProvider extends Provider<Document, Document> {

    protected void processValueProviderSerializations(HashMap<String, Object> data, Supply supply) {
        for (ValueProvider<?, ?, ?> p : Colossus.getDatabaseDriver().getValueProviders(getStockName())) {
            MongoValueProvider<?> provider = (MongoValueProvider<?>) p;
            data.put(provider.getKeyName(), provider.serialize(supply.get(provider.getKeyName())));
        }
    }

    protected void processValueProviderDeserializations(HashMap<String, Object> values, Document data) {
        for (ValueProvider<?, ?, ?> p : Colossus.getDatabaseDriver().getValueProviders(getStockName())) {
            MongoValueProvider<?> provider = (MongoValueProvider<?>) p;
            values.put(provider.getKeyName(), provider.deserialize(data));
        }
    }
}
