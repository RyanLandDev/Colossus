package net.ryanland.colossus.sys.file.database;

import net.ryanland.colossus.Colossus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Stock {

    private final String name;
    private final HashMap<PrimaryKey, Supply> suppliers;

    public Stock(String name, HashMap<PrimaryKey, Supply> suppliers) {
        this.name = name;
        this.suppliers = suppliers;
    }

    public String getName() {
        return name;
    }

    public HashMap<PrimaryKey, Supply> getSuppliers() {
        return suppliers;
    }

    /**
     * Gets the {@link Supply} associated with the provided {@link PrimaryKey} in this {@link Stock}
     * @param key The {@link PrimaryKey}
     */
    public Supply get(PrimaryKey key) {
        return suppliers.get(key);
    }

    /**
     * Gets the {@link Supply} in this {@link Stock} associated with the provided key.
     * <p>The {@link PrimaryKey} for this {@link Stock} must consist of 1 {@link String} to use this method.
     * @param key The {@link Supply} key
     */
    public Supply get(String key) {
        return get(new PrimaryKey(Map.of(suppliers.keySet().iterator().next().keys().keySet().iterator().next(), key)));
    }

    /**
     * Inserts a new {@link Supply} in the database and adds it to the local cache
     * @param supply The {@link Supply} to insert
     * @return The {@link Supply} inserted
     */
    public Supply insert(Supply supply) {
        return Colossus.getDatabaseDriver().insert(supply);
    }

    /**
     * Adds a {@link Supply} to this {@link Stock} (does not update the database)
     */
    public Supply cache(Supply supply) {
        suppliers.put(supply.getPrimaryKey(), supply);
        return supply;
    }

    public void removeFromCache(PrimaryKey key) {
        suppliers.remove(key);
    }

    /**
     * Serializes the provided {@link Supply} and updates it in the provided {@link Stock} in the database.
     * @param supply The {@link Supply} to update, must have the same pointer as the one in cache
     */
    public void update(Supply supply) {
        Colossus.getDatabaseDriver().updateSupply(supply);
    }

    /**
     * Updates a {@link Supply} in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param key The {@link PrimaryKey} associated with the supply to modify
     * @param supplyModifier The supply modifier function; providing the old value and returning the new value
     * @see #update(Supply)
     */
    public void modify(PrimaryKey key, Function<Supply, Supply> supplyModifier) {
        update(supplyModifier.apply(get(key)));
    }

    /**
     * Updates a {@link Supply} in the database with modified values.
     * <br>Provides the old value to help with modification.
     * <p>The {@link PrimaryKey} for this {@link Stock} must consist of 1 {@link String} to use this method.
     * @param key The key associated with the supply to modify
     * @param supplyModifier The supply modifier function; providing the old value and returning the new value
     * @see #update(Supply)
     */
    public void modify(String key, Function<Supply, Supply> supplyModifier) {
        update(supplyModifier.apply(get(key)));
    }

    /**
     * Deletes a {@link Supply} from the database and the cache
     * @param supply The {@link Supply} to delete
     */
    public void delete(Supply supply) {
        Colossus.getDatabaseDriver().delete(supply);
    }
}
