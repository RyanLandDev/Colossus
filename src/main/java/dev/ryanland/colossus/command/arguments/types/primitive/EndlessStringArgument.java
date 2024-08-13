package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public class EndlessStringArgument extends Argument<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        ArgumentOptionData data = new ArgumentOptionData(OptionType.NUMBER);
        if (min != null) data.setMinValue(min);
        if (max != null) data.setMaxValue(max);
        return data;
    }

    private Integer min;
    private Integer max;

    public EndlessStringArgument() {
    }

    public EndlessStringArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public EndlessStringArgument setMinimum(int min) {
        this.min = min;
        return this;
    }

    public EndlessStringArgument setMaximum(int max) {
        this.max = max;
        return this;
    }

    private String check(String value) throws ArgumentException {
        if (value.length() >= min && value.length() <= max)
            return value;
        else
            throw new MalformedArgumentException("Argument must be longer than " + min +
                " characters and shorter than " + max + " characters.");
    }

    @Override
    public CompletableFuture<String> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(check(args.pop().getAsString()));
        return future;
    }

    @Override
    public CompletableFuture<String> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(check(String.join(" ", args)));
        args.clear();
        return future;
    }
}
