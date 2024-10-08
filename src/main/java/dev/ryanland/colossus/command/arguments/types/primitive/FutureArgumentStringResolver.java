package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.types.FutureSingleArgument;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.CompletableFuture;

/**
 * Same as {@link ArgumentStringResolver},
 * except that its methods' return types must be {@link CompletableFuture<T>} instead of just {@link T}
 */
public abstract class FutureArgumentStringResolver<T> extends FutureSingleArgument<T> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.STRING);
    }

    @Override
    public final CompletableFuture<T> resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return resolve(arg.getAsString(), event);
    }

    @Override
    public final CompletableFuture<T> resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return resolve(arg, event);
    }

    public abstract CompletableFuture<T> resolve(String arg, CommandEvent event) throws ArgumentException;
}
