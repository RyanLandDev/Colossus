package net.ryanland.colossus.sys.file.config;

import net.ryanland.colossus.ColossusBuilder;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.Map;

public interface Config {

    Map<String, Object> values = new LinkedMap<>();

    /**
     * Reads the config.<br>
     * This method is called internally when instantiating a {@link ColossusBuilder} object
     * and upon calling {@link ColossusBuilder#build()}, and should normally never be called in your code.
     * @implNote Colossus automatically adds a couple configuration entries in addition to your own. See {@link ColossusBuilder#CORE_CONFIG_ENTRIES}.
     */
    void read();

    /**
     * Adds the given key+value pairs to the config.
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
     * Returns the {@link Boolean} associated with this key.
     */
    default Boolean getBoolean(String key) {
        return (Boolean) values.get(key);
    }
}
