package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public abstract class FutureSingleArgument<T> extends Argument<T> {

    @Override
    public CompletableFuture<T> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        return resolveSlashCommandArgument(args.pop(), event);
    }

    @Override
    public CompletableFuture<T> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        return resolveMessageCommandArgument(args.pop(), event);
    }

    public abstract CompletableFuture<T> resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException;

    public abstract CompletableFuture<T> resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException;
}
