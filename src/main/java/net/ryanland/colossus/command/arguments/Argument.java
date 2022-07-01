package net.ryanland.colossus.command.arguments;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.Deque;
import java.util.function.Function;

public abstract class Argument<T> {

    private String name;
    private String id;
    private String description;
    private boolean optional = false;
    private Function<CommandEvent, T> optionalFunction = event -> null;

    public Argument<T> id(String id) {
        this.id = id;
        return this;
    }

    public Argument<T> optional() {
        optional = true;
        return this;
    }

    public Argument<T> optional(Function<CommandEvent, T> defaultValue) {
        optional = true;
        optionalFunction = defaultValue;
        return this;
    }

    public Argument<T> description(String description) {
        this.description = description;
        return this;
    }

    public OptionData getOptionData() {
        return getArgumentOptionData()
            .setName(name == null ? id : name)
            .setDescription(description)
            .setRequired(!isOptional());
    }

    public String getName() {
        return name == null ? id : name;
    }

    public String getId() {
        return id;
    }

    public boolean isOptional() {
        return optional;
    }

    public Function<CommandEvent, T> getOptionalFunction() {
        return optionalFunction;
    }

    public String getDescription() {
        return description;
    }

    public abstract ArgumentOptionData getArgumentOptionData();

    public abstract T resolveSlashCommandArgument(Deque<OptionMapping> args, SlashEvent event) throws ArgumentException;

    public abstract T resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException;
}
