package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.local.LocalFile;
import net.ryanland.colossus.sys.file.local.LocalFileBuilder;
import net.ryanland.colossus.sys.file.local.LocalFileType;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import net.ryanland.colossus.sys.file.database.Stock;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JSON implementation of {@link DatabaseDriver}.<br>
 * <b>Warning:</b> Using JSON as a database is very unreliable and is <i>not recommended</i>.
 */
public class JsonDatabaseDriver extends DatabaseDriver {

    private final LocalFile dir;
    private Map<String, LocalFile> files;

    public JsonDatabaseDriver(String databaseDirectory) {
        dir = LocalFile.validateDirectoryPath(databaseDirectory);
        files = Map.of(
            "members", getFile(databaseDirectory, "members"),
            "users", getFile(databaseDirectory, "users"),
            "guilds", getFile(databaseDirectory, "guilds"),
            "global", getFile(databaseDirectory, "global")
        );
    }

    private LocalFile getFile(String databaseDirectory, String stockName) {
        return new LocalFileBuilder()
            .setName(databaseDirectory + "/" + stockName)
            .setFileType(LocalFileType.JSON)
            .setDefaultContent(isArray(stockName) ? "[]" : "{}")
            .buildFile();
    }

    private boolean isArray(String stockName) {
        return getPrimaryKeys().get(stockName).size() != 1;
    }

    @Override
    protected Stock findStock(String stockName) {
        JsonElement json = files.get(stockName).parseJson();
        HashMap<PrimaryKey, Supply> suppliers = new HashMap<>();

        Iterable<JsonElement> iter = json.isJsonArray() ? json.getAsJsonArray() :
            json.getAsJsonObject().entrySet().stream().map(Map.Entry::getValue).toList();
        for (JsonElement entry : iter) {
            Supply supply = Colossus.getProvider(stockName).deserialize(entry.getAsJsonObject()).setStockName(stockName);
            suppliers.put(supply.getPrimaryKey(), supply);
        }

        return new Stock(stockName, suppliers);
    }

    @Override
    protected void deleteStock(String stockName) {
        files.get(stockName).delete();
    }

    @Override
    public Supply insertSupply(Supply supply) {
        supply = supply.getProvider().deserialize(supply.serialize());

        JsonObject obj = supply.serialize();
        LocalFile file = files.get(supply.getStockName());
        JsonElement json = file.parseJson();

        if (isArray(supply.getStockName())) {
            Supply oldSupply = findStock(supply.getStockName()).getSuppliers().get(supply.getPrimaryKey());
            if (oldSupply != null) json.getAsJsonArray().remove(oldSupply.serialize());
            json.getAsJsonArray().add(obj);
        } else {
            String firstKey = getPrimaryKeys().get(supply.getStockName()).get(0);
            json.getAsJsonObject().remove(firstKey);
            json.getAsJsonObject().add(supply.get(firstKey), obj);
        }

        file.write(json);
        return supply;
    }

    @Override
    public void updateSupply(Supply supply) {
        insertSupply(supply);
    }

    @Override
    protected void deleteSupply(Supply supply) {
        JsonObject obj = supply.serialize();
        LocalFile file = files.get(supply.getStockName());
        JsonElement json = file.parseJson();

        if (isArray(supply.getStockName())) {
            json.getAsJsonArray().remove(obj);
        } else {
            json.getAsJsonObject().remove(getPrimaryKeys().get(supply.getStockName()).get(0));
        }

        file.write(json);
    }

    public <T> JsonDatabaseDriver registerValueProvider(String stockName, String keyName,
                                                         Function<T, JsonObject> serializer, Function<JsonObject, T> deserializer) {
        registerValueProviders(new JsonValueProvider<T>() {
            @Override
            public String getStockName() {
                return stockName;
            }

            @Override
            public String getKeyName() {
                return keyName;
            }

            @Override
            public JsonObject serialize(T toSerialize) {
                return serializer.apply(toSerialize);
            }

            @Override
            public T deserialize(JsonObject data) {
                return deserializer.apply(data);
            }
        });
        return this;
    }
}
