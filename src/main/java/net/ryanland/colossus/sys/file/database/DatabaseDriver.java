package net.ryanland.colossus.sys.file.database;

import net.ryanland.colossus.ColossusBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract class for creating "database drivers". Either use a pre-made one or create your own.<br>
 * When creating your own, do not forget to create {@link Provider Providers} for the (default pre-defined) values.
 * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
 */
public abstract class DatabaseDriver {

    // Stock methods -----------------------

    protected final HashMap<String, Stock> stockCache = new HashMap<>();

    /**
     * Attempts getting the {@link Stock} from the local cache.
     * If this is null, tries retrieving it from the database. If the {@link Stock} was not found in the database,
     * null is returned.
     *
     * <p>In other words, returns the {@link Stock} associated with the provided name in the cache,
     * or if null, returns {@link #find(String)}.
     *
     * @param stockName The name of the {@link Stock}
     * @return The result {@link Stock}. This will never be null.
     */
    public Stock get(String stockName) {
        return nullOr(getFromCache(stockName), () -> find(stockName));
    }

    /**
     * Gets the {@link Stock} associated with the provided name from the local cache.
     * @param stockName The name of the {@link Stock}
     * @return The found {@link Stock}, {@code null} if no {@link Stock} is currently in the cache with the provided name
     */
    public Stock getFromCache(String stockName) {
        return stockCache.get(stockName);
    }

    /**
     * Retrieves the {@link Stock} associated with the provided name from the database and adds it to the local cache.
     * @param stockName The name of the {@link Stock}
     * @return The found {@link Stock}, {@code null} if it doesn't exist
     */
    public Stock find(String stockName) {
        Stock stock = findStock(stockName);
        if (stock == null) return null;
        cache(stock);
        return stock;
    }

    /**
     * Retrieves the data associated with the provided name from the database,
     * and then deserializes it to a {@link Stock}.
     * @param stockName The name of the {@link Stock}
     * @return The found {@link Stock}, {@code null} if it doesn't exist
     */
    protected abstract Stock findStock(String stockName);

    /**
     * Deletes the {@link Stock} associated with the provided name from the database, and the local cache.
     * @param stockName The name of the {@link Stock}
     */
    public void delete(String stockName) {
        deleteStock(stockName);
        removeFromCache(stockName);
    }

    /**
     * Deletes the {@link Stock} associated with the provided name from the database.
     */
    protected abstract void deleteStock(String stockName);

    /**
     * Adds a {@link Stock} to the local cache.
     * @param stock The {@link Stock} to cache
     */
    public void cache(Stock stock) {
        stockCache.put(stock.getName(), stock);
    }

    /**
     * Removes a {@link Stock} from the local cache.
     * @param stockName The name of the {@link Stock}
     */
    public void removeFromCache(String stockName) {
        stockCache.remove(stockName);
    }

    // Supply methods ---------------------------

    /**
     * Inserts a new {@link Supply} in the database and adds it to the local cache
     * @param supply The {@link Supply} to insert
     * @return The {@link Supply} inserted
     */
    public Supply insert(Supply supply) {
        insertSupply(supply);
        // serialize + deserialize for default values
        Supply newSupply = get(supply.getStockName()).get(supply.getPrimaryKey());
        cache(newSupply);

        return newSupply;
    }

    /**
     * Serializes a new {@link Supply} and inserts it in the database.<br>
     * Note: Does not update the cache. Use {@link #insert(Supply)} or {@link #cache(Supply)} instead.
     * @param supply The {@link Supply} to insert
     * @return The {@link Supply} inserted
     * @see #insert(Supply)
     */
    public abstract Supply insertSupply(Supply supply);

    /**
     * Serializes the provided {@link Supply} and updates it in the provided {@link Stock} in the database.
     * @param supply The {@link Supply} to update, must have the same pointer as the one in cache
     */
    public abstract void updateSupply(Supply supply);

    /**
     * Deletes a {@link Supply} from the database and the cache
     * @param supply The {@link Supply} to delete
     */
    public void delete(Supply supply) {
        deleteSupply(supply);
        removeFromCache(supply.getStockName(), supply.getPrimaryKey());
    }

    /**
     * Deletes a {@link Supply} from the database
     * @param supply The {@link Supply} to delete
     */
    protected abstract void deleteSupply(Supply supply);

    public void cache(Supply supply) {
        getFromCache(supply.getStock().getName()).cache(supply);
    }

    public void removeFromCache(String stockName, PrimaryKey key) {
        getFromCache(stockName).removeFromCache(key);
    }

    // Misc methods ------------------

    public Map<String, List<String>> getPrimaryKeys() {
        return Map.of("global", List.of("_bot_id"),
            "guilds", List.of("_guild_id"),
            "users", List.of("_user_id"),
            "members", List.of("_user_id", "_guild_id"),
            "cooldowns", List.of("user_id", "command_name", "command_type"),
            "disabled_commands", List.of("command_name", "command_type"));
    }

    /**
     * Alternative to {@link Objects#requireNonNullElse(Object, Object)} with the use of a {@link Supplier},
     * allowing the second value's code to be executed only if the first value is null.
     */
    public static <T> T nullOr(T object, Supplier<T> compareTo) {
        return object != null ? object : compareTo.get();
    }
}
