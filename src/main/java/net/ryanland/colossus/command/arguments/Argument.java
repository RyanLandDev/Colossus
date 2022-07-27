package net.ryanland.colossus.command.arguments;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.InterruptedArgumentException;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public abstract class Argument<T> {

    private String name;
    private String description;
    private boolean optional = false;
    private Function<CommandEvent, CompletableFuture<T>> optionalFunction = event -> null;

    public final Argument<T> name(String name) {
        this.name = name;
        return this;
    }

    public final Argument<T> optional() {
        optional = true;
        return this;
    }

    public final Argument<T> optionalFuture(Function<CommandEvent, CompletableFuture<T>> defaultValue) {
        optional = true;
        optionalFunction = defaultValue;
        return this;
    }

    public final Argument<T> optional(Function<CommandEvent, T> defaultValue) {
        CompletableFuture<T> future = new CompletableFuture<>();
        return optionalFuture(event -> {
            future.complete(defaultValue.apply(event));
            return future;
        });
    }

    public final Argument<T> description(String description) {
        this.description = description;
        return this;
    }

    public final OptionData getOptionData() {
        if (getName() == null || getDescription() == null) {
            throw new IllegalStateException(getClass().getName() + " - Arguments must have at least a name and description.");
        }
        return getArgumentOptionData()
            .setName(getName())
            .setDescription(getDescription())
            .setRequired(!isOptional());
    }

    public final String getName() {
        return name;
    }

    public final boolean isOptional() {
        return optional;
    }

    public final Function<CommandEvent, CompletableFuture<T>> getOptionalFunction() {
        return optionalFunction;
    }

    public final T getOptionalValue(CommandEvent event) throws ArgumentException {
        try {
            return getOptionalFunction().apply(event).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedArgumentException();
        }
    }

    public final String getDescription() {
        return description;
    }

    // ------------------------

    public abstract ArgumentOptionData getArgumentOptionData();

    public boolean ignoreMissingException() {
        return false;
    }

    public abstract CompletableFuture<T> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException;

    public abstract CompletableFuture<T> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException;

    public final T resolveSlashCommandArgument(SlashCommandEvent event, Deque<OptionMapping> args) throws ArgumentException {
        try {
            return resolveSlashCommandArgument(args, event).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedArgumentException();
        }
    }

    public final T resolveMessageCommandArgument(MessageCommandEvent event, Deque<String> args) throws ArgumentException {
        try {
            return resolveMessageCommandArgument(args, event).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedArgumentException();
        }
    }
}
