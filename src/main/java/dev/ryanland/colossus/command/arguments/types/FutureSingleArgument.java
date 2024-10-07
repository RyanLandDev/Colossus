package dev.ryanland.colossus.command.arguments.types;

import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public abstract class FutureSingleArgument<T> extends Argument<T> {

    @Override
    public CompletableFuture<T> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        return resolveMessageCommandArgument(args.pop(), event);
    }

    public abstract CompletableFuture<T> resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException;

    public abstract CompletableFuture<T> resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException;
}
