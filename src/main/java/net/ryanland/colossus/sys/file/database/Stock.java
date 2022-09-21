package net.ryanland.colossus.sys.file.database;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.provider.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.ryanland.colossus.sys.file.database.DatabaseDriver.nullOr;

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

    public Provider<?> getProvider() {
        return Colossus.getProvider(getName());
    }

    /**
     * Gets the {@link Supply} associated with the provided {@link PrimaryKey} in this {@link Stock}.<br>
     * Attempts getting the {@link Supply} from the locally cached {@link Stock}.
     * If this is null, a new {@link Supply} will be inserted into the database and this {@link Supply} will be returned.
     * @param key The {@link PrimaryKey}
     * @return The result {@link Supply}. This will never be null.
     * @see #get(String)
     */
    public Supply get(PrimaryKey key) {
        return nullOr(suppliers.get(key), () -> insert(new Supply(getName(), key.keys())));
    }

    /**
     * Gets the {@link Supply} in this {@link Stock} associated with the provided key.
     * Attempts getting the {@link Supply} from the locally cached {@link Stock}.
     * If this is null, a new {@link Supply} will be inserted into the database and this {@link Supply} will be returned.
     * <p>The {@link PrimaryKey} for this {@link Stock} must consist of 1 {@link String} to use this method.
     * @param key The {@link Supply} key
     * @return The result {@link Supply}. This will never be null.
     * @see #get(PrimaryKey)
     */
    public Supply get(String key) {
        List<String> primaryKeys = Colossus.getDatabaseDriver().getPrimaryKeys().get(getName());
        if (primaryKeys.size() > 1) {
            throw new IllegalArgumentException("The PrimaryKey for this Stock must consist of 1 String to use this method.");
        }
        return get(new PrimaryKey(Map.of(primaryKeys.get(0), key)));
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
