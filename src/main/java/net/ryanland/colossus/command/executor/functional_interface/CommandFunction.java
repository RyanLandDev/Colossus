package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.Function;

@FunctionalInterface
public interface CommandFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        throw new IllegalStateException("Use CommandFunction#run instead");
    }

    R run(T t) throws CommandException;
}
