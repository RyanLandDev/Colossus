package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashCommandEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QuoteStringArgument extends Argument<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.STRING);
    }

    @Override
    public CompletableFuture<String> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(args.pop().getAsString());
        return future;
    }

    @Override
    public CompletableFuture<String> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        String ERROR_MESSAGE = "Invalid quote, must start and end with `'` or `\"`.";

        // If the argument does not begin with a quote, throw an error
        if (!(args.element().startsWith("'") || args.peek().startsWith("\"")))
            throw new MalformedArgumentException(ERROR_MESSAGE);

        // Repeat over all arguments until one ends with a quote
        List<String> elements = new ArrayList<>();
        String argument = args.poll();
        elements.add(argument);
        while (!(argument.endsWith("'") || argument.endsWith("\""))) {
            argument = args.poll();
            if (argument == null)
                throw new MalformedArgumentException(ERROR_MESSAGE);
            elements.add(argument);
        }

        // Join elements and remove quotes
        String result = String.join(" ", elements);
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(result.substring(1, result.length()-1));
        return future;
    }
}
