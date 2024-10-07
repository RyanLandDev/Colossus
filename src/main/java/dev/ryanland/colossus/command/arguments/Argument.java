package dev.ryanland.colossus.command.arguments;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.InterruptedArgumentException;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
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
            CompletableFuture<T> value = getOptionalFunction().apply(event);
            if (value == null) return null;
            else return value.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedArgumentException();
        }
    }

    public final String getDescription() {
        return description;
    }

    // ------------------------

    public abstract ArgumentOptionData getArgumentOptionData();

    public Argument<T> addChoice(@NotNull String name, double value) {
        getArgumentOptionData().addChoice(name, value);
        return this;
    }

    public Argument<T> addChoice(@NotNull String name, long value) {
        getArgumentOptionData().addChoice(name, value);
        return this;
    }

    public Argument<T> addChoice(@NotNull String name, @NotNull String value) {
        getArgumentOptionData().addChoice(name, value);
        return this;
    }

    public Argument<T> addChoices(String... choices) {
        getArgumentOptionData().addChoices(choices);
        return this;
    }

    public Argument<T> addChoices(List<String> choices) {
        getArgumentOptionData().addChoices(choices);
        return this;
    }

    public Argument<T> addChoices(@NotNull Command.Choice... choices) {
        getArgumentOptionData().addChoices(choices);
        return this;
    }

    public Argument<T> addChoices(@NotNull Collection<? extends Command.Choice> choices) {
        getArgumentOptionData().addChoices(choices);
        return this;
    }

    public boolean ignoreMissingException() {
        return false;
    }

    public abstract CompletableFuture<T> resolveSlashCommandArgument(OptionMapping option, SlashCommandEvent event) throws ArgumentException;

    public abstract CompletableFuture<T> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException;

    public final T resolveSlashCommandArgument(SlashCommandEvent event, OptionMapping option) throws ArgumentException {
        try {
            return resolveSlashCommandArgument(option, event).get();
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
