package net.ryanland.colossus.command.arguments.parsing.functional_interface;

import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ArgumentBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    default R apply(T t, U u) {
        throw new IllegalStateException("Use ArgumentBiFunction#run instead");
    }

    R run(T t, U u) throws ArgumentException;
}
