package net.ryanland.colossus.sys.file.config;

import net.ryanland.colossus.Colossus;

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
     * Returns the {@link Boolean} associated with this key.
     * <p>Equivalent of {@code Colossus.getConfig().getBoolean(key)}.
     */
    public static Boolean getBoolean(String key) {
        return Colossus.getConfig().getBoolean(key);
    }
}
