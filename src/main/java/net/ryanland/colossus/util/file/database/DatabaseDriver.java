package net.ryanland.colossus.util.file.database;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public abstract class DatabaseDriver {

    public abstract TableCache<?>[] getCaches();

    public <R extends Table<T>, T extends ISnowflake> R get(T client) {
        return get(client, false);
    }

    public <R extends Table<T>, T extends ISnowflake> R get(T client, boolean nullIfMissing) {

    }

    /**
     * Alternative to {@link Objects#requireNonNullElse(Object, Object)} with {@link Supplier}
     */
    public static <T> T nullOr(T object, Supplier<T> compareTo) {
        return object != null ? object : compareTo.get();
    }

}
