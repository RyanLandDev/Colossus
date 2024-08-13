package net.ryanland.colossus.sys.config;

import net.ryanland.colossus.ColossusBuilder;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

/**
 * Represents a Configuration Supplier. Implementations take care of reading and saving configuration files.
 * <br>Colossus provides {@link JsonConfig} by default.
 * <p>For easily accessing values, use {@link Config}.
 */
public interface ConfigSupplier {

    Map<String, Object> values = new LinkedMap<>();

    /**
     * Reads the config.<br>
     * This method is called internally when instantiating a {@link ColossusBuilder} object
     * and upon calling {@link ColossusBuilder#build()}, and should normally never be called in your code.
     * @implNote Colossus automatically adds a couple configuration entries in addition to your own. See {@link ColossusBuilder#CORE_CONFIG_ENTRIES}.
     */
    void read();

    /**
     * Puts the given key+value pair in the config.
     */
    default <V> void addValue(String key, V defaultValue) {
        this.values.put(key, defaultValue);
    }

    /**
     * Puts the given key+value pairs in the config.
     */
    default <V> void addValues(Map<String, V> values) {
        this.values.putAll(values);
    }

    /**
     * Returns the value associated with this key.
     */
    default Object get(String key) {
        return values.get(key);
    }

    /**
     * Returns the {@link String} associated with this key.
     */
    default String getString(String key) {
        return (String) values.get(key);
    }

    /**
     * Returns the {@link Integer} associated with this key.
     */
    default Integer getInt(String key) {
        return (Integer) values.get(key);
    }

    /**
     * Returns the {@link Long} associated with this key.
     */
    default Long getLong(String key) {
        return (Long) values.get(key);
    }

    /**
     * Returns the {@link Float} associated with this key.
     */
    default Float getFloat(String key) {
        return (Float) values.get(key);
    }

    /**
     * Returns the {@link Double} associated with this key.
     */
    default Double getDouble(String key) {
        return (Double) values.get(key);
    }

    /**
     * Returns the {@link Boolean} associated with this key.
     */
    default Boolean getBoolean(String key) {
        return (Boolean) values.get(key);
    }

    /**
     * Returns the {@link java.util.ArrayList} associated with this key.
     */
    default <R> List<R> getList(String key) {
        return (List<R>) values.get(key);
    }
}
