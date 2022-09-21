package net.ryanland.colossus.sys.file.database;

import net.ryanland.colossus.Colossus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Supply {

    private final PrimaryKey primaryKey;
    private String stockName;
    private final HashMap<String, Object> values;

    private List<String> modifiedKeys = new ArrayList<>();

    public Supply(HashMap<String, Object> values) {
        this.values = values;

        List<String> primaryKeys = Colossus.getDatabaseDriver().getPrimaryKeys().get(stockName);
        HashMap<String, Object> primaryKeyMap = new HashMap<>();
        for (String key : primaryKeys) {
            primaryKeyMap.put(key, get(key));
        }
        primaryKey = new PrimaryKey(primaryKeyMap);
    }

    public Supply(PrimaryKey primaryKey, String stockName, HashMap<String, Object> values) {
        this.primaryKey = primaryKey;
        this.stockName = stockName;
        this.values = values;
    }

    public Supply setStockName(String stockName) {
        this.stockName = stockName;
        return this;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public String getStockName() {
        return stockName;
    }

    public Stock getStock() {
        return Colossus.getDatabaseDriver().get(stockName);
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    public List<String> getModifiedKeys() {
        return modifiedKeys;
    }

    @SuppressWarnings("unchecked")
    public <R> R serialize() {
        return (R) Colossus.getProvider(getStockName()).serialize(this);
    }

    /**
     * Gets the value associated with the provided key
     */
    @SuppressWarnings("unchecked")
    public <R> R get(String key) {
        return (R) values.get(key);
    }

    /**
     * Gets the value associated with the provided key, or if null, returns the provided default value
     */
    @SuppressWarnings("unchecked")
    public <R> R get(String key, R defaultValue) {
        return (R) values.getOrDefault(key, defaultValue);
    }

    /**
     * Updates/adds a value to this {@link Supply}.<br>
     * Note: Does not update the database, only updates the cache. Use {@link #push()} to update the database.
     * @see #modify(String, Function)
     * @see #push(String, Object)
     * @see #push()
     */
    public <V> Supply put(String key, V newValue) {
        values.put(key, newValue);
        modifiedKeys.add(key);
        return this;
    }

    /**
     * Updates/adds a value to this {@link Supply}, and gives the old value to help with modification.<br>
     * Note: Does not update the database, only updates the cache. Use {@link #push()} to update the database.
     * @see #put(String, Object)
     * @see #push(String, Object)
     * @see #push()
     */
    public <V> Supply modify(String key, Function<V, V> modifier) {
        return put(key, modifier.apply(get(key)));
    }

    /**
     * Removes a value from this {@link Supply}.
     * Note: Does not update the database, only updates the cache. Use {@link #push()} to update the database.
     */
    public Supply remove(String key) {
        values.remove(key);
        modifiedKeys.add(key);
        return this;
    }

    /**
     * Puts a new value in this {@link Supply} and then pushes this {@link Supply} to the database
     */
    public <V> void push(String key, V newValue) {
        put(key, newValue);
        push();
    }

    /**
     * Pushes all changes in this {@link Supply} to the database
     */
    public void push() {
        Colossus.getDatabaseDriver().updateSupply(this);
        modifiedKeys = new ArrayList<>();
    }

    /**
     * Deletes this {@link Supply} from the cache and the database
     */
    public void delete() {
        getStock().delete(this);
    }
}
