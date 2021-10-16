package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CommandBiConsumer<T, U> extends BiConsumer<T, U> {

    @Override
    default void accept(T t, U u) {
        throw new IllegalStateException("Use CommandBiConsumer#consume instead");
    }

    void consume(T t, U u) throws CommandException;
}
