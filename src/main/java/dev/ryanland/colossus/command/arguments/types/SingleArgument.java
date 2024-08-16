package dev.ryanland.colossus.command.arguments.types;

import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public abstract class SingleArgument<T> extends Argument<T> {

    @Override
    public final CompletableFuture<T> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.complete(resolveSlashCommandArgument(args.pop(), event));
        return future;
    }

    @Override
    public final CompletableFuture<T> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.complete(resolveMessageCommandArgument(args.pop(), event));
        return future;
    }

    public abstract T resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException;

    public abstract T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException;
}