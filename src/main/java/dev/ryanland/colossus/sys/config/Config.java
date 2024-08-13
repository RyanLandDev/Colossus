package dev.ryanland.colossus.sys.config;

import dev.ryanland.colossus.Colossus;

import java.util.List;

/**
 * Shortcut helper class.
 */
public final class Config {

    /**
     * Returns the value associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().get(key)}.
     */
    public static Object get(String key) {
        return Colossus.getConfig().get(key);
    }

    /**
     * Returns the {@link String} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getString(key)}.
     */
    public static String getString(String key) {
        return Colossus.getConfig().getString(key);
    }

    /**
     * Returns the {@link Integer} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getInt(key)}.
     */
    public static Integer getInt(String key) {
        return Colossus.getConfig().getInt(key);
    }

    /**
     * Returns the {@link Long} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getLong(key)}.
     */
    public static Long getLong(String key) {
        return Colossus.getConfig().getLong(key);
    }

    /**
     * Returns the {@link Float} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getFloat(key)}.
     */
    public static Float getFloat(String key) {
        return Colossus.getConfig().getFloat(key);
    }

    /**
     * Returns the {@link Double} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getFloat(key)}.
     */
    public static Double getDouble(String key) {
        return Colossus.getConfig().getDouble(key);
    }

    /**
     * Returns the {@link Boolean} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getBoolean(key)}.
     */
    public static Boolean getBoolean(String key) {
        return Colossus.getConfig().getBoolean(key);
    }

    /**
     * Returns the {@link java.util.ArrayList} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getList(key)}.
     */
    public static <R> List<R> getList(String key) {
        return Colossus.getConfig().getList(key);
    }
}
