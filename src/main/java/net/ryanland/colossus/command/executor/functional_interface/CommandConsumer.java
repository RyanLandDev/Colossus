package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.Consumer;

@FunctionalInterface
public interface CommandConsumer<T> extends Consumer<T> {

    @Override
    default void accept(T t) {
        throw new IllegalStateException("Use CommandConsumer#consume instead");
    }

    void consume(T t) throws CommandException;
}
