package net.ryanland.colossus.sys.oldfile.provider;

import java.util.List;

public abstract class ListProvider<D, P> extends Provider<List<D>, P> {

    public abstract boolean contains(D value);

    public abstract void add(D value);

    public abstract void remove(D value);
}
