package net.ryanland.colossus.bot.command.executor.functional_interface;

import net.ryanland.colossus.bot.command.CommandException;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface CommandFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        throw new IllegalStateException("Use CommandFunction#run instead");
    }

    R run(T t) throws CommandException;
}
