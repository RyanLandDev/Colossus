package net.ryanland.colossus.sys.oldfile.provider;

import org.jetbrains.annotations.Nullable;

public abstract class Provider<D, P> {

    public abstract String getKey();

    public abstract D retrieve(@Nullable P param);

    public final D retrieve() {
        return retrieve(null);
    }

    public abstract void update(D newValue);
}
