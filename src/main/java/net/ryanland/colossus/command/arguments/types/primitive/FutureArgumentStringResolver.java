package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.types.FutureSingleArgument;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashCommandEvent;

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
