package dev.ryanland.colossus.sys.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Node<T extends Node<T>> implements Iterable<T> {

    private final List<T> children = new ArrayList<>();
    private T parent;

    @SuppressWarnings("all")
    private T setParent(T parent) {
        this.parent = parent;
        return (T) this;
    }

    @SuppressWarnings("all")
    public T addChildren(T... children) {
        this.children.addAll(Arrays.stream(children)
            .map(child -> ((Node<T>) child).setParent((T) this)).toList());
        return (T) this;
    }

    public List<T> getChildren() {
        return children;
    }

    public T getParent() {
        return parent;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return children.iterator();
    }
}
