package net.ryanland.colossus.sys.file.database.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import net.ryanland.colossus.sys.file.database.Stock;
import net.ryanland.colossus.sys.file.database.Supply;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * MongoDB implementation of {@link DatabaseDriver}
 */
public class MongoDatabaseDriver extends DatabaseDriver {

    private final String secret;
    private final MongoClient client;
    private final MongoDatabase database;
    private final Map<String, MongoCollection<Document>> collections;

    /**
     * Create an instance of {@link MongoDatabaseDriver}.<br>
     * Note: MongoDB collections won't be created for you
     * @param secret The secret connection string
     * @param databaseName The name of the database to connect to
     */
    public MongoDatabaseDriver(String secret, String databaseName) {
        this.secret = secret;
        client = MongoClients.create(this.secret);
        database = client.getDatabase(databaseName);
        collections = Map.of(
            "members", database.getCollection("members"),
            "users", database.getCollection("users"),
            "guilds", database.getCollection("guilds"),
            "global", database.getCollection("global")
        );
    }

    public MongoCollection<Document> getCollection(String name) {
        return collections.get(name);
    }

    @Override
    protected Stock findStock(String stockName) {
        HashMap<PrimaryKey, Supply> suppliers = new HashMap<>();
        for (Document document : getCollection(stockName).find()) {
            Supply supply = Colossus.getProvider(stockName).deserialize(document).setStockName(stockName);
            suppliers.put(supply.getPrimaryKey(), supply);
        }
        return new Stock(stockName, suppliers);
    }

    @Override
    protected void deleteStock(String stockName) {
        throw new UnsupportedOperationException("To delete a collection in the Mongo database, use the dashboard");
    }

    @Override
    public Supply insertSupply(Supply supply) {
        getCollection(supply.getStockName()).insertOne(supply.serialize());
        return supply;
    }

    private Bson toFilter(Supply supply) {
        Document document = supply.serialize();
        return Filters.and(supply.getPrimaryKey().keys().keySet().stream()
            .map(key -> Filters.eq(key, document.get(key))).toArray(Bson[]::new));
    }

    @Override
    public void updateSupply(Supply supply) {
        Document document = supply.serialize();
        List<Bson> updates = supply.getModifiedKeys().stream().map(key -> Updates.set(key, document.get(key))).toList();
        getCollection(supply.getStockName()).updateOne(toFilter(supply), updates);
    }

    @Override
    protected void deleteSupply(Supply supply) {
        getCollection(supply.getStockName()).deleteOne(toFilter(supply));
    }

    public <T> MongoDatabaseDriver registerValueProvider(String stockName, String keyName,
                                                         Function<T, Object> serializer, Function<Document, T> deserializer) {
        registerValueProviders(new MongoValueProvider<T>() {
            @Override
            public String getStockName() {
                return stockName;
            }

            @Override
            public String getKeyName() {
                return keyName;
            }

            @Override
            public Object serialize(T toSerialize) {
                return serializer.apply(toSerialize);
            }

            @Override
            public T deserialize(Document data) {
                return deserializer.apply(data);
            }
        });
        return this;
    }
}
